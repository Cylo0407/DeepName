package com.example.deepname.Service;

import com.example.deepname.Utils.MyResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 接收文件
     *
     * @param username 用户名
     * @param file     文件
     */
    MyResponse upload(String username, MultipartFile file);


    /**
     * 通过url下载文件
     *
     * @param username 用户名
     * @param url      路径;
     */
    MyResponse downLoadFromUrl(String username, String url);

    /**
     * 遍历目录结构
     *
     * @param dirpath 目录路径;
     */
    MyResponse getDir(String dirpath);

    /**
     * 返回文件文本
     *
     * @param filepath 文件路径;
     */
    MyResponse getFileCtx(String filepath);

    /**
     * 返回readme内容
     *
     * @param dirpath 目录路径;
     */
    MyResponse getPreView(String dirpath);
}
