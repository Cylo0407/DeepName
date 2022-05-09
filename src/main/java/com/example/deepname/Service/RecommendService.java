package com.example.deepname.Service;

import com.example.deepname.Utils.MyResponse;

public interface RecommendService {

    /**
     * 调用python程序
     *
     * @param filepath 文件路径;
     */
    MyResponse getPyService(String filepath);

    /**
     * 调用参数拓展
     *      *
     * @param filepath 文件路径
     */
    MyResponse getParamExpand(String filepath);

}
