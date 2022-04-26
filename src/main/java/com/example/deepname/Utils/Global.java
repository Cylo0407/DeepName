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
    public static String localUrl = "/Users/cyl/deepname/src/Files/";
    public static String ZipPath = "/Users/cyl/deepname/src/FileZips/";
    public static String ResPath = "/Users/cyl/deepname/src/Res/";

    public static String preName = "DeepName/Data/Zips/";

    //阿里云API的内或外网域名
    public static String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    //阿里云API的密钥Access Key ID
    public static String ACCESS_KEY_ID = "LTAI5tQRPhJDaEqJxazowqov";
    //阿里云API的密钥Access Key Secret
    public static String ACCESS_KEY_SECRET = "bBiYNPrmYjA1pmH5tKtt4XKqHQqVVH";
    //阿里云API的bucket名称
    public static String BACKET_NAME = "nju";

}
