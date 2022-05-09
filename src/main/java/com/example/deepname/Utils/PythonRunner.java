package com.example.deepname.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonRunner {
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
            proc1.destroy();
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
            proc2.destroy();
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
            proc3.destroy();
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
            proc4.destroy();
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

            proc_test.waitFor();
            proc_test.destroy();
            System.out.println("end_all");
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
