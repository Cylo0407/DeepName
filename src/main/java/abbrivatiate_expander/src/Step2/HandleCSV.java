package abbrivatiate_expander.src.Step2;

import abbrivatiate_expander.src.Global.LX;
import abbrivatiate_expander.src.Step0.PreOperation;
import abbrivatiate_expander.src.Step1.ExtractAST;

import abbrivatiate_expander.src.Wiki.Worm;

import abbrivatiate_expander.src.expansion.AllExpansions;
import com.example.deepname.Utils.Levenshtein;
import com.example.deepname.VO.AbbreviationRecommendVO;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class HandleCSV {
    public static void main(String[] args) throws IOException {
        recommendProcess(LX.javaSource);
    }

    public static ArrayList<AbbreviationRecommendVO> recommendProcess(String srcPath) throws IOException {
        File file = new File(srcPath);
        String filename = file.getName();
        if (!filename.contains(".java")) {
            return new ArrayList<AbbreviationRecommendVO>();
        } else {
            String fileParent = file.getParent().replaceAll("\\\\", "/");
            String trimPath = fileParent + "/trim_" + filename;
            String tempPath = fileParent + "/temp_" + filename.substring(0, filename.length() - 5) + ".csv";
            String resPath = fileParent + "/res_" + filename;
            PreOperation.preOperation(srcPath, trimPath, tempPath);
            ExtractAST.parseCode(trimPath, tempPath);
            ArrayList<AbbreviationRecommendVO> res = HandleCSV.recommend(srcPath, tempPath, resPath);
            try {
                File trimFile = new File(trimPath);
                File tempFile = new File(tempPath);
                File resFile = new File(resPath);
                if (trimFile.exists()) {
                    if (trimFile.delete()) {
                        System.out.println("trim deleted.");
                    }
                } else {
                    System.out.println("trim not found.");
                }
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        System.out.println("temp deleted.");
                    }
                } else {
                    System.out.println("temp not found.");
                }
                if (resFile.exists()) {
                    if (resFile.delete()) {
                        System.out.println("res deleted.");
                    }
                } else {
                    System.out.println("res not found.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            AllExpansions.reInit();
            return res;
        }
    }

    public static ArrayList<AbbreviationRecommendVO> recommend(String srcPath, String tempPath, String destPath) throws IOException {
        HashMap<String, ArrayList<String>> fileData = readParseResult(tempPath);
        ArrayList<AbbreviationRecommendVO> resList = new ArrayList<AbbreviationRecommendVO>();

        for (String id : fileData.keySet()) {
//          id 当前检测的内容
//			System.out.println(id);
            ArrayList<String> value = fileData.get(id);

            String nameOfIdentifier = value.get(1);
            if (nameOfIdentifier.trim().equals(""))
                continue;
//          把驼峰命名法的名称转为下划线分割，并分词
            String[] partsRaw = (Util.CamelToUnderline(new StringBuffer(nameOfIdentifier))).toString().split("_");

            ArrayList<String> parts = new ArrayList<String>();
            for (String string : partsRaw) {
                if (!string.equals(""))
                    parts.add(string);
            }
//          当前检测的parts，parts来自AST树中的一条的分词结果
            System.out.println("name=" + nameOfIdentifier);

            StringBuilder expansionFull = new StringBuilder();
            ArrayList<String> possiWordArrayList = null;
            String methodName = "";
            for (String part : parts) {
//              当前检测的部分
//				System.out.println(part);
//				System.out.println("part=" + part);
//                	Wiki.Worm.wikili.put(part, Wiki.Worm.Wiki(part));

                possiWordArrayList = new ArrayList<String>();
                // Dictionary
                if (isInEnglishDic(part)) {
                    continue;
                }
                isInAbbrDic(possiWordArrayList, part);
                isIncomputerAbbr(possiWordArrayList, part);

                // Dic

                for (int i = 3; i < 17; i++) // lx '<='-->'<'
                {
                    if (i != 9 && !value.get(i).equals("")) {
                        String rawValue = value.get(i);

                        System.out.println("raw=" + rawValue);
                        if (rawValue.contains(";")) {
                            String[] values = rawValue.split(";");
                            for (String string : values) {
                                String[] valueSplit = string.split(":");
                                if (valueSplit.length <= 1)
                                    continue;
                                String valuePrefix = valueSplit[0];
                                String trueValue = valueSplit[1];
                                System.out.println("Value=" + trueValue);
                                if (valuePrefix.equals("MethodName")) {
                                    methodName = trueValue;
                                }
                                if (Heu.H1(part, trueValue) || Heu.H2(part, trueValue)
                                        || Heu.H3(part, trueValue)) {
                                    System.err.println("param=" + trueValue);
                                    if (!possiWordArrayList.contains(trueValue))
                                        possiWordArrayList.add(trueValue);
                                }
                            }
                        }
                    }
                    if (i == 9) {
                        String comment = value.get(i);
//							System.err.println(comment);
                        for (String string : PossibleExpansionFromComment(part, comment)) {
                            if (!possiWordArrayList.contains(string))
                                possiWordArrayList.add(string);
                        }
                    }
                    if (i == 16) {
                        StringBuilder possiExp = new StringBuilder();
                        possiExp.append(part);
                        for (String string : possiWordArrayList) {
                            possiExp.append("(").append(string).append(")");
                        }
//                        if (possiWordArrayList.size() == 0) {
//                            // 当找不到推荐词汇时，从wiki中查询该词汇
//
//                            HashSet<String> wiki = Worm.Wiki(part);
//                            if (wiki != null)
//                                for (String string : wiki) {
//                                    System.out.println("wiki=" + string);
//                                }
//                            if (wiki != null)
//                                for (String string : wiki) {
//                                    possiExp.append("[").append(string).append("]");
//                                }
//                        }
                        possiExp.append(";");
                        expansionFull.append(possiExp);
                    }
                }

                // 推荐信息
                for (String string : possiWordArrayList) {
                    System.out.println("possi=" + string);
                }

            }
            System.out.println("******");
            if (!methodName.equals("")) {
                System.out.println("Parameter name:" + nameOfIdentifier);
                System.out.println("Method name:" + methodName);
                System.out.println("Possible recommend names:" + String.join(",", possiWordArrayList));
                if (possiWordArrayList.size() > 0) {
                    // fix
                    HashMap<String, Float> map = new HashMap<>();
                    for (int i = 0; i <= possiWordArrayList.size(); i++) {
                        String recommendName = possiWordArrayList.get(i);
                        Float distance = Levenshtein.getSimilarity(nameOfIdentifier, recommendName);
                        map.put(recommendName, distance);
                    }
                    resList.add(new AbbreviationRecommendVO(nameOfIdentifier, methodName, map));
                }
            } else {
                System.out.println("Has no recommend.");
            }
            System.out.println("******");
            System.out.println("=============================================");
            WriteNode.writerNodes.add(new WriteNode(id, nameOfIdentifier + "-->" + expansionFull));
        }
//        FileReader(srcPath, destPath);
        return resList;

        /*
         * 前端需要的数据格式：
         * [
         *   {
         *       param_name:String,
         *       method_name:String,
         *       possible_recommends:String[]
         *   }
         * ]
         * */

    }

    static BufferedReader br;
    static BufferedWriter bw;

    public static void FileReader(String srcPath, String destPath) throws IOException {
        br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcPath))));
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(destPath))));
        String line;
        int lineNum = 0;
        while ((line = br.readLine()) != null) {
            lineNum++;
            bw.write(line);
            for (String id : LX.IdLineInFile.keySet()) {
                if (LX.IdLineInFile.get(id) == lineNum) {
//					System.out.println(lineNum + "||" + id + "||" + WriteNode.getCommentByID(id));
                    String exp = WriteNode.getCommentByID(id);

                    if (!exp.trim().equals("")) {
                        bw.write("//");
                        bw.write(exp);
                        System.out.println(exp);
                    }
                    break;
                }
            }
            bw.write("\n");
        }
        bw.flush();
    }

    // englishDicHashSet\computerAbbrDicHashMap

    public static boolean isInEnglishDic(String word) {
        if (word.length() == 1) {
            return false;
        }
        return Dic.englishDicHashSet.contains(word.toLowerCase());
    }

    public static void isInAbbrDic(ArrayList<String> possiWordList, String word) {
        for (String string : Dic.abbrDicHashMap.keySet()) {
            if (string.equals(word)) {
                if (!possiWordList.contains(word))
                    possiWordList.add(Dic.abbrDicHashMap.get(word));
            }
        }

    }

    public static void isIncomputerAbbr(ArrayList<String> possiWordList, String word) {
        for (String string : Dic.computerAbbrDicHashMap.keySet()) {
            if (string.equals(word)) {
                String compDic = Dic.computerAbbrDicHashMap.get(word);
                for (String string2 : compDic.split(";")) {
                    if (!possiWordList.contains(string2))
                        possiWordList.add(string2);
                }
            }
        }
    }

    public static ArrayList<String> PossibleExpansionFromComment(String subWord, String comment) {
        ArrayList<String> temp = new ArrayList<String>();

        comment = comment.replaceAll(";", " ");
        String[] fullWords = comment.split(" "); // 注释中所有的单词
        for (String naturalWord : fullWords) {
            if (Heu.H1(subWord, naturalWord) || Heu.H2(subWord, naturalWord)
                    || Heu.H3(subWord, naturalWord)) {
                temp.add(naturalWord);
            }
        }
        return temp;
    }

    private static HashMap<String, ArrayList<String>> readParseResult(String fileName) {
        HashMap<String, ArrayList<String>> idToInfos = new HashMap<>();
        ArrayList<String> lines = readFile(fileName);
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] temp = line.split(",", -1);
            if (idToInfos.containsKey(temp[0])) {
//					System.err.println(temp[0]);
//					System.err.println("duplicate keys");
                continue;
            }
            // id name typeOfIdentifier Assign Extend MethodDeclaration MethodInvocation
            // Type Comment
            ArrayList<String> value = new ArrayList<>();
            for (int j = 1; j < temp.length; j++) {
                value.add(temp[j]);
            }
            idToInfos.put(temp[0], value);
        }
        return idToInfos;
    }

    // convert text file to ArrayList<String> line by line
    public static ArrayList<String> readFile(String fileName) {
        ArrayList<String> result = new ArrayList<String>();
        File file = new File(fileName);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                if (!tempString.equals("")) {
                    result.add(tempString);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}