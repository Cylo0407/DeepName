package com.example.deepname.Service.Impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.example.deepname.Service.FileService;
import com.example.deepname.Utils.Global;
import com.example.deepname.Utils.utils;
import com.example.deepname.Utils.MyResponse;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;


@Service
@Transactional
public class FileServiceImpl implements FileService {
    private static final String EMPTY_FILE = "上传文件不能为空!";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

    @Override
    public MyResponse upload(MultipartFile file) {
        if (file.isEmpty())
            return MyResponse.buildFailure(EMPTY_FILE);

        String format = sdf.format(new Date());
        File folder = new File(Global.ZipPath + format);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String filename = file.getOriginalFilename();
        try {
            file.transferTo(new File(folder, filename));
            utils.unPack(Global.ZipPath + format + filename);
            return MyResponse.buildSuccess(Global.localUrl + format + filename);
        } catch (IOException e) {
            e.printStackTrace();
            return MyResponse.buildFailure(e.getMessage());
        }
//        try {
//            String fileName = file.getOriginalFilename();
//            String uuid = UUID.randomUUID().toString();
//            fileName = uuid + '-' + fileName;
//            String format = new DateTime().toString("yyyy/MM/dd");
//            String filepath = Global.preName + format + "/" + fileName;  // 拼接文件完整路径: 2020/01/03/parker.jpg
//            System.out.println(filepath);
//            // 获取上传文件输入流
//            InputStream in = file.getInputStream();
//
//            OSS ossClient = new OSSClient(Global.ENDPOINT, Global.ACCESS_KEY_ID, Global.ACCESS_KEY_SECRET);
//            ossClient.putObject(Global.BACKET_NAME, filepath, in);
//            ossClient.shutdown();
//
//            String path = "http://" + Global.BACKET_NAME + "." + Global.ENDPOINT + "/" + filepath;
//
//            return MyResponse.buildSuccess(path);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return MyResponse.buildFailure(e.getMessage());
//        }
    }


    @Override
    public MyResponse uploadFloder(MultipartFile[] files) {
        MyResponse response = new MyResponse();
        if (files == null || files.length == 0) {
            response.setIsSuccess(false);
            response.setMsg(EMPTY_FILE);
            return response;
        }
        response.setIsSuccess(true);

        String format = sdf.format(new Date());
        File folder = new File(Global.ZipPath + format);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }

        for (MultipartFile file : files) {
            String filePath = Global.ZipPath + format + file.getOriginalFilename();
            makeDir(filePath);
            File dest = new File(filePath);
            try {
                file.transferTo(dest);
            } catch (IllegalStateException | IOException e) {
                response.setIsSuccess(false);
                response.setMsg(e.getMessage());
                e.printStackTrace();
            }
        }

        return response;
    }

    /**
     * 确保目录存在，不存在则创建
     *
     * @param filePath
     */
    private static void makeDir(String filePath) {
        if (filePath.lastIndexOf('/') > 0) {
            String dirPath = filePath.substring(0, filePath.lastIndexOf('/'));
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }


    @Override
    public MyResponse downLoadFromUrl(String url) {
        MyResponse response = new MyResponse();
        try {
            System.out.println("开始下载主仓。。。");
            CloneCommand cc = Git.cloneRepository().setURI(url);
            cc.setCredentialsProvider(Global.credentialsProvider);
            String format = sdf.format(new Date());
            String filename = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
            String path = Global.localUrl + format + filename;
            cc.setDirectory(new File(path)).call();
            System.out.println("主仓下载完成。。。");

            response.setIsSuccess(true);
            response.setData(path);
        } catch (Exception e) {
            response.setIsSuccess(false);
            response.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public MyResponse getDir(String dirpath) {
        File srcFile = new File(dirpath);
        File[] fileArray = srcFile.listFiles();
        List<String> filenames = new LinkedList<>();
        for (int i = 0; i < fileArray.length; i++) {
            filenames.add(fileArray[i].toString());
//            System.out.println(fileArray[i].toString());
        }
        return MyResponse.buildSuccess(filenames);
    }

    @Override
    public MyResponse getFileCtx(String filepath) {
        File javaFile = new File(filepath);
        StringBuilder result = new StringBuilder();
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(javaFile), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String s = null;
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
            br.close();
            System.out.println(result.toString());
            return MyResponse.buildSuccess(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return MyResponse.buildFailure(e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        String url = "/Users/cyl/deepname/src/main/java/com/example/deepname/Utils/utils.java";
//        getFileCtx(url);
//    }

}
