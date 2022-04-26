package com.example.deepname.Service;

import com.example.deepname.Utils.MyResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 接收文件
     * @param file 文件
     */
    MyResponse upload(MultipartFile file);

    /**
     * 接收文件夹
     * @param files 文件夹
     */
    MyResponse uploadFloder(MultipartFile[] files);


    /**
     * 通过url下载文件
     * @param url 路径;
     */
    MyResponse downLoadFromUrl(String url);

    /**
     * 遍历目录结构
     * @param dirpath 目录路径;
     */
    MyResponse getDir(String dirpath);

    /**
     * 返回文件文本
     * @param filepath 文件路径;
     */
    MyResponse getFileCtx(String filepath);
}
