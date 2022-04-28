package com.example.deepname.Service.Impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.example.deepname.Entity.Record;
import com.example.deepname.Exception.BussinessException;
import com.example.deepname.Repository.RecordRepository;
import com.example.deepname.Service.FileService;
import com.example.deepname.Service.RecordService;
import com.example.deepname.Utils.Global;
import com.example.deepname.Utils.VPMapper.RecordMapper;
import com.example.deepname.Utils.utils;
import com.example.deepname.Utils.MyResponse;
import com.example.deepname.VO.DirVO;
import com.example.deepname.VO.RecordVO;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;

import javax.annotation.Resource;


@Service
@Transactional
public class FileServiceImpl implements FileService {
    private static final String EMPTY_FILE = "上传文件不能为空!";

    @Resource
    private RecordRepository recordRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

    @Override
    public MyResponse upload(String username, MultipartFile file) {
        System.out.println(file);
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

            Record record = new Record();
            record.setUsername(username);
            record.setFilename(filename);
            record.setFilepath(Global.localUrl + format + filename);
            return MyResponse.buildSuccess(RecordMapper.INSTANCE.p2v(recordRepository.save(record)));
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
    public MyResponse downLoadFromUrl(String username, String url) {
        try {
            System.out.println("开始下载主仓。。。");
            CloneCommand cc = Git.cloneRepository().setURI(url);
            cc.setCredentialsProvider(Global.credentialsProvider);
            String format = sdf.format(new Date());
            String filename = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
            String path = Global.localUrl + format + filename;
            cc.setDirectory(new File(path)).call();
            System.out.println("主仓下载完成。。。");

            Record record = new Record();
            record.setUsername(username);
            record.setFilename(filename);
            record.setFilepath(path);
            System.out.println("记录添加完成");
            return MyResponse.buildSuccess(RecordMapper.INSTANCE.p2v(recordRepository.save(record)));
        } catch (Exception e) {
            e.printStackTrace();
            return MyResponse.buildFailure(e.getMessage());
        }
    }

    @Override
    public MyResponse getDir(String dirpath) {
        File srcFile = new File(dirpath);
        File[] fileArray = srcFile.listFiles();
//        List<String> filenames = new LinkedList<>();
        DirVO dirVO = new DirVO();
        List<String> files = new LinkedList<>();
        List<String> dirs = new LinkedList<>();
        for (int i = 0; i < fileArray.length; i++) {
            if ((fileArray[i].toString()).contains("."))
                files.add(fileArray[i].toString());
            else dirs.add(fileArray[i].toString());
//            filenames.add(fileArray[i].toString());
//            System.out.println(fileArray[i].toString());
        }
        int idx = dirpath.lastIndexOf('/');
        dirVO.setParentPath(dirpath.substring(0,idx));
        dirVO.setFiles(files);
        dirVO.setDirs(dirs);
        return MyResponse.buildSuccess(dirVO);
    }

    @Override
    public MyResponse getFileCtx(String filepath) {
        File javaFile = new File(filepath);
        StringBuilder result = new StringBuilder();
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(javaFile), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String s = "";
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
//
//    }

}
