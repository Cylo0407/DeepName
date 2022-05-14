package com.example.deepname.Service.Impl;

import abbrivatiate_expander.src.Step2.HandleCSV;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.example.deepname.Entity.Record;
import com.example.deepname.Exception.BussinessException;
import com.example.deepname.Repository.RecordRepository;
import com.example.deepname.Service.FileService;
import com.example.deepname.Service.RecordService;
import com.example.deepname.Utils.Global;
import com.example.deepname.Utils.PythonRunner;
import com.example.deepname.Utils.VPMapper.RecordMapper;
import com.example.deepname.Utils.utils;
import com.example.deepname.Utils.MyResponse;
import com.example.deepname.VO.AbbreviationRecommendVO;
import com.example.deepname.VO.DirVO;
import com.example.deepname.VO.MethodNameRecommendVO;
import com.example.deepname.VO.RecordVO;
import org.eclipse.jgit.api.errors.JGitInternalException;
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
import javax.rmi.CORBA.Util;

@Service
@Transactional
public class FileServiceImpl implements FileService {
    private static final String EMPTY_FILE = "上传文件目录为空!";
    private static final String EXCLUDED_JAVA = "项目中不包含任何java文件，导入失败";
    private static final String EXCLUDED_README = "README文件获取失败，该项目可能没有README.md";

    @Resource
    private RecordRepository recordRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

    @Override
    public MyResponse upload(String username, MultipartFile file) {
        if (file.isEmpty())
            return MyResponse.buildFailure(EMPTY_FILE);

        String format = sdf.format(new Date());
        File folder = new File(Global.ZipPath + format);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String filename = file.getOriginalFilename();
        String prename = filename.substring(0, filename.length() - 4);
        String zipPath = Global.ZipPath + format + filename;
        String filePath = Global.localUrl + format + prename;
        System.out.println(filePath);
        try {
            file.transferTo(new File(folder, filename));
            System.out.println(zipPath);
            utils.unPack(zipPath);

            if (!utils.hasjava(filePath))
                return MyResponse.buildFailure(EXCLUDED_JAVA);

            Record record = new Record();
            record.setUsername(username);
            record.setFilename(prename);
            record.setFilepath(filePath);
            return MyResponse.buildSuccess(RecordMapper.INSTANCE.p2v(recordRepository.save(record)));
        } catch (IOException e) {
            e.printStackTrace();
            return MyResponse.buildFailure(e.getMessage());
        }
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

            if (!utils.hasjava(path))
                return MyResponse.buildFailure(EXCLUDED_JAVA);

            Record record = new Record();
            record.setUsername(username);
            record.setFilename(filename);
            record.setFilepath(path);
            System.out.println("记录添加完成");
            return MyResponse.buildSuccess(RecordMapper.INSTANCE.p2v(recordRepository.save(record)));
        } catch (JGitInternalException e) {
            return MyResponse.buildFailure("����Ŀ�Ѿ����ڣ������ظ�����");
        } catch (Exception e) {
            e.printStackTrace();
            return MyResponse.buildFailure(e.getMessage());
        }
    }

    @Override
    public MyResponse getDir(String dirpath) {
        File srcFile = new File(dirpath);
        File[] fileArray = srcFile.listFiles();
        DirVO dirVO = new DirVO();
        List<String> javaFiles = new LinkedList<>();
        List<String> otherFiles = new LinkedList<>();
        List<String> dirs = new LinkedList<>();
        for (int i = 0; i < fileArray.length; i++) {
            if ((fileArray[i].toString()).contains(".")) {
                if ((fileArray[i].toString()).endsWith(".java"))
                    javaFiles.add(fileArray[i].toString());
                else otherFiles.add(fileArray[i].toString());
            } else dirs.add(fileArray[i].toString());
        }
        int idx = dirpath.lastIndexOf('/');
        dirVO.setParentPath(dirpath.substring(0, idx));
        dirVO.setJavaFiles(javaFiles);
        dirVO.setOtherFiles(otherFiles);
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
            String s = br.readLine();
            if (s != null) {
                result.append(s);
                while ((s = br.readLine()) != null) {
                    result.append(System.lineSeparator()).append(s);
                }
            }
            br.close();
            System.out.println(result.toString());
            return MyResponse.buildSuccess(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return MyResponse.buildFailure(e.getMessage());
        }
    }

    @Override
    public MyResponse getPreView(String dirpath) {
        File readme = new File(dirpath + '/' + "README.md");
        if (!readme.exists())
            return MyResponse.buildFailure(EXCLUDED_README);

        StringBuilder result = new StringBuilder();
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(readme), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String s = br.readLine();
            if (s != null) {
                result.append(s);
                while ((s = br.readLine()) != null) {
                    result.append(System.lineSeparator()).append(s);
                }
            }
            br.close();
            return MyResponse.buildSuccess(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return MyResponse.buildFailure(e.getMessage());
        }
    }


//    public static void main(String[] args) {
//        String dirpath = "/Users/cyl/DeepName-2021-ICSE";
//        MyResponse response = getPreView(dirpath);
//        System.out.println(response.getData());
//    }

}
