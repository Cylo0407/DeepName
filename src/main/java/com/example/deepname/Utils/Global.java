package com.example.deepname.Utils;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class Global {

    //gitee账号
    public static String username = "Cheng0407";
    //gitee密码
    private static String password = "Cheng0407";
    public static CredentialsProvider credentialsProvider =
            new UsernamePasswordCredentialsProvider(username, password);

    //文件目录
    public static String localUrl = "E:/Projects/datasets_dp/test_data/deepname/files/";
    public static String ZipPath = "E:/Projects/datasets_dp/test_data/deepname/zips/";
    public static String ResPath = "E:/Projects/datasets_dp/test_data/deepname/res/";




    public static String preName = "DeepName/Data/Zips/";

    //阿里云API的内或外网域名
    public static String ENDPOINT = "";
    //阿里云API的密钥Access Key ID
    public static String ACCESS_KEY_ID = "";
    //阿里云API的密钥Access Key Secret
    public static String ACCESS_KEY_SECRET = "bBiYNPrmYjA1pmH5tKtt4XKqHQqVVH";
    //阿里云API的bucket名称
    public static String BACKET_NAME = "nju";

}
