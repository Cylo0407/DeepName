package com.example.deepname.Service;

import com.example.deepname.Utils.MyResponse;
import com.example.deepname.VO.UserVO;
import org.springframework.web.multipart.MultipartFile;

public interface NameDebugService {
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
}
