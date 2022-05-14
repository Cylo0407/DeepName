package com.example.deepname.Utils;

import abbrivatiate_expander.src.Step1.ExtractAST;
import abbrivatiate_expander.src.Step2.HandleCSV;
import abbrivatiate_expander.src.expansion.AllExpansions;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class utils {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

    //解压zip文件到Files目录
    public static void unPack(String zipPath) {
        System.out.println(zipPath);
        try {
            ZipFile zipfile = new ZipFile(zipPath);
            zipfile.setFileNameCharset("GBK");
            String format = sdf.format(new Date());
            File folder = new File(Global.ZipPath + format);
            if (!folder.isDirectory()) {
                folder.mkdirs();
            }
            zipfile.extractAll(Global.localUrl + format);
            deleteDirectory(new File(Global.localUrl + format + "__MACOSX"));
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    //删除无用目录
    public static void deleteDirectory(File dirFile) {
        if (dirFile.exists()) {
            if (dirFile.isFile()) {
                dirFile.delete();
            } else {
                for (File file : dirFile.listFiles()) {
                    System.out.println(file.getName());
                    deleteDirectory(file);
                }
                dirFile.delete();
            }
        }
    }

    //判断是否含有java文件
    public static boolean hasjava(String path) {
        File dir = new File(path);
        List<String> filenames = new ArrayList<>();
        visitAllDirsAndFiles(dir, filenames);
        for (String filename : filenames) {
            if (filename.endsWith(".java")) return true;
        }
        return false;
    }

    //遍历目录
    public static void visitAllDirsAndFiles(File path, List<String> filenames) {
        if (path.isDirectory()) {
            String[] children = path.list();
            for (int i = 0; i < children.length; i++) {
                visitAllDirsAndFiles(new File(path, children[i]), filenames);
            }
        } else filenames.add(path.toString());
    }

    //返回方法行数
    public static String getLocation(String filename, String methodName, String signature) {
        try {
            HashMap<String, ArrayList<String>> fileData = HandleCSV.readParseResult(Global.csvPath + "temp_" + filename + ".csv");
            for (String id : fileData.keySet()) {
                ArrayList<String> value = fileData.get(id);
                // 如果不是方法则跳过
                String type = value.get(2);
                if (type.equals("MethodName")) {
                    String sourceMethodName = value.get(1);
                    if (sourceMethodName.equals(methodName)) {
                        String parameters = value.get(15);
                        ArrayList<String> params = new ArrayList<>(Arrays.asList(parameters.replaceAll("ParameterName:", "").split(";")));

                        Pattern pattern = Pattern.compile(methodName + "([^)]*)");
                        Matcher matcher = pattern.matcher(signature);
                        ArrayList<String> signatureParams = new ArrayList<String>();
                        if (matcher.find()) {
                            signatureParams.addAll(Arrays.asList(matcher.group().split(",")));
                        }
                        for (int i = 0; i < signatureParams.size(); i++) {
                            String[] temp = signatureParams.get(i).split(" ");
                            signatureParams.set(i, temp[temp.length - 1]);
                        }
                        if (params.containsAll(signatureParams) && signatureParams.containsAll(params)) {
                            // 如果方法名相同返回行号
                            return value.get(17);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-1";
    }

    public static void main(String[] args) {
        String filename = "ActionBar";
        String methodName = "addAction";
        String signature = "    public void addAction(Action action, int index)";
        getLocation(filename, methodName, signature);
    }
}
