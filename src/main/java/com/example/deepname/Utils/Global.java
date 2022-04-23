package com.example.deepname.Utils;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class Global {

    //gitee账号
    public static String username = "Cheng0407";
    //gitee密码
    private static String password = "Cheng0407";
    public static CredentialsProvider credentialsProvider =
            new UsernamePasswordCredentialsProvider(username,password);

    //文件目录
    public static String localUrl = "/Users/cyl/deepname/src/Files/";
    public static String ZipPath = "/Users/cyl/deepname/src/FileZips/";
    public static String ResPath = "/Users/cyl/deepname/src/Res/";
}
