package com.example.deepname.Utils;

import com.jcraft.jsch.IO;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static void step1(String dirpath, String filename){
        Process proc1;
        try {
            String cmd1 = "python D:/GTNM/GTNM/data_processing/merge_project.py "
                    + dirpath + ' ' + filename;
            proc1 = Runtime.getRuntime().exec(cmd1);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc1.waitFor();
        } catch (IOException |InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void step2(String filename){
        Process proc2;
        try {
            String cmd2 = "python D:/GTNM/GTNM/data_processing/processor.py " +
                    filename;
            proc2 = Runtime.getRuntime().exec(cmd2);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc2.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc2.waitFor();
        } catch (IOException |InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void step3(String input_file_name, String output_file_name){
        Process proc3;
        try {
            String cmd3 = "python D:/GTNM/GTNM/data_processing/extract_data.py " +
                    "--input_file_name "+input_file_name+ ' ' +
                    "--output_file_name "+output_file_name;
            proc3 = Runtime.getRuntime().exec(cmd3);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc3.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc3.waitFor();
        } catch (IOException |InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void step4(String filename){
        Process proc4;
        try {
            String cmd4 = "python D:/GTNM/GTNM/data_processing/invoked_save.py " + filename;
            proc4 = Runtime.getRuntime().exec(cmd4);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc4.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc4.waitFor();
        } catch (IOException |InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void step_test(String filename){
        final Process proc_test;
        try {
            String cmd_test = "python D:/GTNM/GTNM/Model/test.py --filename " + filename;
            System.out.println("exec");
            proc_test = Runtime.getRuntime().exec(cmd_test);// 执行py文件
            // 防止缓冲区满, 导致卡住
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String line;
                    try {
                        BufferedReader stderr = new BufferedReader(new InputStreamReader(proc_test.getErrorStream(),"GBK"));
                        while ((line = stderr.readLine()) != null) {
                            System.out.println("stderr:" + line);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }.start();

            System.out.println("==============打印结果==============");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String line;
                    try {
                        BufferedReader stdout = new BufferedReader(new InputStreamReader(proc_test.getInputStream(),"GBK"));
                        while ((line = stdout.readLine()) != null) {
                            System.out.println("stdout:" + line);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

//            BufferedReader in = new BufferedReader(new InputStreamReader(proc_test.getInputStream()));
//            System.out.println("sss");
//            String line = "";
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//            in.close();
            proc_test.waitFor();
            System.out.println("end_all");
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
