package abbrivatiate_expander.src.Step2;

import abbrivatiate_expander.src.Global.LX;
import abbrivatiate_expander.src.Step1.ExtractAST;

import abbrivatiate_expander.src.expansion.AllExpansions;
import com.example.deepname.Utils.Global;
import com.example.deepname.Utils.Levenshtein;
import com.example.deepname.VO.AbbreviationRecommendVO;
import com.example.deepname.Utils.utils;

import java.io.*;
import java.util.*;

public class HandleCSV {
    public static void main(String[] args) throws IOException {
        recommendProcess(LX.javaSource);
    }

    public static ArrayList<ArrayList<AbbreviationRecommendVO>> recommendProcess(String srcPath) throws IOException {
        File file = new File(srcPath);
        String filename = file.getName();

        String filePath = Global.csvPath;
        String tempPath = filePath + "temp_" + filename.substring(0, filename.length() - 5) + ".csv";
        ExtractAST.parseCode(srcPath, tempPath);
        ArrayList<AbbreviationRecommendVO> declarationParams = HandleCSV.recommendMethodDeclarationParams(tempPath);
        ArrayList<AbbreviationRecommendVO> invokedParams = HandleCSV.recommendMethodInvokedParams(tempPath);
        try {
            File tempFile = new File(tempPath);
            if (tempFile.exists()) {
                if (tempFile.delete()) {
                    System.out.println("temp deleted.");
                }
            } else {
                System.out.println("temp not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<ArrayList<AbbreviationRecommendVO>> res = new ArrayList<ArrayList<AbbreviationRecommendVO>>();
        res.add(declarationParams);
        res.add(invokedParams);
        AllExpansions.reInit();
        return res;
    }

    public static ArrayList<AbbreviationRecommendVO> recommendMethodInvokedParams(String tempPath) throws IOException {
        HashMap<String, ArrayList<String>> fileData = readParseResult(tempPath);
        ArrayList<AbbreviationRecommendVO> resList = new ArrayList<AbbreviationRecommendVO>();
        recommendMethodInvokedParams(fileData, resList);
        return resList;
    }

    public static ArrayList<AbbreviationRecommendVO> recommendMethodDeclarationParams(String tempPath) throws IOException {
        HashMap<String, ArrayList<String>> fileData = readParseResult(tempPath);
        ArrayList<AbbreviationRecommendVO> resList = new ArrayList<AbbreviationRecommendVO>();
        recommendMethodDeclarationParams(fileData, resList);
        return resList;
    }

    private static void recommendMethodDeclarationParams(HashMap<String, ArrayList<String>> fileData, ArrayList<AbbreviationRecommendVO> resList) {
        // ???????????????id
        for (String id : fileData.keySet()) {
            ArrayList<String> value = fileData.get(id);

            // ???????????????????????????
            String type = value.get(2);
            if (!type.equals("MethodName")) {
                continue;
            }
            // ?????????????????????????????????
            String params = value.get(15);
            if (params.equals("")) {
                continue;
            }

            // ????????????????????????
            String methodName = value.get(1);
            if (methodName.trim().equals(""))
                continue;
            System.out.println("MethodName=" + methodName);

            // ??????????????????
            String[] paramList = params.split(";");
            ArrayList<String> paramNameList = new ArrayList<String>();
            ArrayList<String> paramTypeList = new ArrayList<String>();
            for (String param : paramList) {
                paramNameList.add(param.split(":")[1]);
            }
            // ????????????????????????

            // ????????????
            String locationOfMethod = value.get(17);

            // ?????????????????????
            System.out.println("------------------------------");
            for (String paramName : paramNameList) {
                for (String paramId : fileData.keySet()) {
                    ArrayList<String> paramValue = fileData.get(paramId);
                    // ???????????????????????????
                    if (paramValue.get(2).equals("ParameterName")) {
                        // ??????????????????????????????????????????????????????????????????
                        if (paramValue.get(1).equals(paramName) && paramValue.get(17).equals(locationOfMethod)) {
                            // ??????????????????
//                            paramTypeList.add(paramValue.get());
                            // ????????????????????????????????????????????????????????????
                            String[] partsRaw = (Util.CamelToUnderline(new StringBuffer(paramName))).toString().split("_");
                            ArrayList<String> parts = new ArrayList<String>();
                            for (String string : partsRaw) {
                                if (!string.equals(""))
                                    parts.add(string);
                            }
                            // ???????????????parts???parts??????AST??????????????????????????????
                            System.out.println("paramName=" + paramName);
                            StringBuilder expansionFull = new StringBuilder();

                            ArrayList<String> possibleWordArrayList = new ArrayList<String>();
                            for (String part : parts) {
                                // ?????????????????????
//                            System.out.println(part);
//                            System.out.println("part=" + part);
//                            Wiki.Worm.wikili.put(part, Wiki.Worm.Wiki(part));
                                possibleWordArrayList = new ArrayList<String>();
                                // Dictionary
                                if (isInEnglishDic(part)) {
                                    continue;
                                }
                                isInAbbrDic(possibleWordArrayList, part);
                                isIncomputerAbbr(possibleWordArrayList, part);

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
                                                    if (!possibleWordArrayList.contains(trueValue))
                                                        possibleWordArrayList.add(trueValue);
                                                }
                                            }
                                        }
                                    }
                                    if (i == 9) {
                                        String comment = value.get(i);
//                                    System.err.println(comment);
                                        for (String string : PossibleExpansionFromComment(part, comment)) {
                                            if (!possibleWordArrayList.contains(string))
                                                possibleWordArrayList.add(string);
                                        }
                                    }
                                    if (i == 16) {
                                        StringBuilder possiExp = new StringBuilder();
                                        possiExp.append(part);
                                        for (String string : possibleWordArrayList) {
                                            possiExp.append("(").append(string).append(")");
                                        }
//                                    if (possiWordArrayList.size() == 0) {
//                                        // ?????????????????????????????????wiki??????????????????
//
//                                        HashSet<String> wiki = Worm.Wiki(part);
//                                        if (wiki != null)
//                                            for (String string : wiki) {
//                                                System.out.println("wiki=" + string);
//                                            }
//                                        if (wiki != null)
//                                            for (String string : wiki) {
//                                                possiExp.append("[").append(string).append("]");
//                                            }
//                                    }
                                        possiExp.append(";");
                                        expansionFull.append(possiExp);
                                    }
                                }

                                // ????????????
                                for (String string : possibleWordArrayList) {
                                    System.out.println("possi=" + string);
                                }
                            }
                            if (!methodName.equals("")) {
                                System.out.println("Parameter name:" + paramName);
                                System.out.println("Method name:" + methodName);
                                if (possibleWordArrayList.size() > 0) {
                                    System.out.println("Possible recommendMethodInvokedParams names:" + String.join(",", possibleWordArrayList));
                                    ArrayList<String> recommendsAccuracyType = new ArrayList<String>();
                                    HashMap<String, Float> map = new HashMap<>();
                                    for (String recommendName : possibleWordArrayList) {
                                        Float distance = Levenshtein.getSimilarity(paramName, recommendName);
//                                        recommendsAccuracyType.add(utils.getAccuracyType(distance));
                                        map.put(recommendName, distance);
                                    }
                                    List<Map.Entry<String, Float>> list = new ArrayList<>(map.entrySet());
                                    Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
                                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                                            if ((o1.getValue() - o2.getValue()) > 0)
                                                return 1;
                                            else if ((o1.getValue() - o2.getValue()) == 0)
                                                return 0;
                                            else
                                                return -1;
                                        }
                                    });
                                    ArrayList<String> sortedPossiWordArrayList = new ArrayList<String>();
                                    for (Map.Entry<String, Float> c : list) {
                                        sortedPossiWordArrayList.add(c.getKey());
                                        recommendsAccuracyType.add(utils.getAccuracyType(c.getValue()));
                                    }
                                    resList.add(new AbbreviationRecommendVO(paramName, methodName, sortedPossiWordArrayList, recommendsAccuracyType, locationOfMethod));
                                }
                            } else {
                                System.out.println("Has no recommendMethodInvokedParams.");
                            }
                            WriteNode.writerNodes.add(new WriteNode(id, methodName + "-->" + expansionFull));
                            break;
                        }
                    }
                }
            }
            System.out.println("------------------------------");
        }
    }

    private static void recommendMethodInvokedParams(HashMap<String, ArrayList<String>> fileData, ArrayList<AbbreviationRecommendVO> resList) {
        for (String id : fileData.keySet()) {
            ArrayList<String> value = fileData.get(id);

            // ?????????????????????????????????
            String type = value.get(2);
            if (!type.equals("VariableName")) {
                continue;
            }
            String variableName = value.get(1);

            // ????????????????????????
            String callerMethodName = value.get(16);
            callerMethodName = callerMethodName.substring(0, callerMethodName.length() - 1).split(":")[1];

            // ????????????,???????????????????????????
            String locationOfVariable = value.get(17);
            if (locationOfVariable == null) {
                continue;
            }

            // ????????????????????????????????????????????????????????????
            String[] partsRaw = (Util.CamelToUnderline(new StringBuffer(variableName))).toString().split("_");
            ArrayList<String> parts = new ArrayList<String>();
            for (String string : partsRaw) {
                if (!string.equals(""))
                    parts.add(string);
            }
            // ???????????????parts???parts??????AST??????????????????????????????
            System.out.println("VariableName=" + variableName);

            ArrayList<String> possibleWordArrayList = new ArrayList<String>();
            for (String part : parts) {
                // ?????????????????????
                possibleWordArrayList = new ArrayList<String>();
                // Dictionary
                if (isInEnglishDic(part)) {
                    continue;
                }
                isInAbbrDic(possibleWordArrayList, part);
                isIncomputerAbbr(possibleWordArrayList, part);

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
                                if (Heu.H1(part, trueValue) || Heu.H2(part, trueValue)
                                        || Heu.H3(part, trueValue)) {
                                    System.err.println("param=" + trueValue);
                                    if (!possibleWordArrayList.contains(trueValue))
                                        possibleWordArrayList.add(trueValue);
                                }
                            }
                        }
                    }
                    if (i == 9) {
                        String comment = value.get(i);
                        for (String string : PossibleExpansionFromComment(part, comment)) {
                            if (!possibleWordArrayList.contains(string))
                                possibleWordArrayList.add(string);
                        }
                    }
                    if (i == 16) {
                        StringBuilder possiExp = new StringBuilder();
                        possiExp.append(part);
                        for (String string : possibleWordArrayList) {
                            possiExp.append("(").append(string).append(")");
                        }
                        possiExp.append(";");
                    }
                }

                // ????????????
                for (String string : possibleWordArrayList) {
                    System.out.println("possi=" + string);
                }
            }

            System.out.println("Variable name:" + variableName);
            if (possibleWordArrayList.size() > 0) {
                System.out.println("Possible recommendMethodInvokedParams names:" + String.join(",", possibleWordArrayList));
                ArrayList<String> recommendsAccuracy = new ArrayList<String>();
//                ArrayList<Float> recommendsDistance = new ArrayList<Float>();
                HashMap<String, Float> map = new HashMap<>();
                for (String recommendName : possibleWordArrayList) {
                    Float distance = Levenshtein.getSimilarity(variableName, recommendName);
//                    recommendsAccuracy.add(utils.getAccuracyType(distance));
//                    recommendsDistance.add(distance);
                    map.put(recommendName, distance);
                }
                List<Map.Entry<String, Float>> list = new ArrayList<>(map.entrySet());
                Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
                    public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                        if ((o1.getValue() - o2.getValue()) > 0)
                            return 1;
                        else if ((o1.getValue() - o2.getValue()) == 0)
                            return 0;
                        else
                            return -1;
                    }
                });
                ArrayList<String> sortedPossiWordArrayList = new ArrayList<String>();
                for (Map.Entry<String, Float> c : list) {
                    sortedPossiWordArrayList.add(c.getKey());
                    recommendsAccuracy.add(utils.getAccuracyType(c.getValue()));
                }
                resList.add(new AbbreviationRecommendVO(variableName, callerMethodName, sortedPossiWordArrayList, recommendsAccuracy, locationOfVariable));
            }
        }
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
        String[] fullWords = comment.split(" "); // ????????????????????????
        for (String naturalWord : fullWords) {
            if (Heu.H1(subWord, naturalWord) || Heu.H2(subWord, naturalWord)
                    || Heu.H3(subWord, naturalWord)) {
                temp.add(naturalWord);
            }
        }
        return temp;
    }

    public static HashMap<String, ArrayList<String>> readParseResult(String fileName) {
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