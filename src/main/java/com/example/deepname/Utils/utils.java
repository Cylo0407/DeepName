package com.example.deepname.Utils;

import com.jcraft.jsch.IO;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        for (String filename: filenames){
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
}
