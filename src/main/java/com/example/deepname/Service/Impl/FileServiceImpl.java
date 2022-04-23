package com.example.deepname.Service.Impl;

import com.example.deepname.Service.FileService;
import com.example.deepname.Utils.Global;
import com.example.deepname.Utils.utils;
import com.example.deepname.Utils.MyResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;

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

        MyResponse response = new MyResponse();
        String format = sdf.format(new Date());
        File folder = new File(Global.ZipPath + format);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String filename = file.getOriginalFilename();
        try {
            file.transferTo(new File(folder, filename));
            response.setIsSuccess(true);
            utils.unPack(Global.ZipPath + format + filename);
            response.setData(Global.localUrl + format + filename);
        } catch (IOException e) {
            response.setIsSuccess(false);
            response.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return response;
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


    //    @Override
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

//    public static void main(String[] args) {
//        String url = "https://gitee.com/cheng0407/section1.git";
//        downLoadFromUrl(url);
//    }
}
