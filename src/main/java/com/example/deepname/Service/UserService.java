package com.example.deepname.Service;

import com.example.deepname.Entity.User;
import com.example.deepname.Utils.MyResponse;
import com.example.deepname.VO.UserVO;


import java.util.List;

public interface UserService {
    /**
     * 用户注册
     * @param userVO 用户对象
     */
    MyResponse register(UserVO userVO);


    /**
     * 用户名登录
     * @param userVO 用户对象
     */
    MyResponse loginByUsername(UserVO userVO);

    /**
     * 用户邮箱登录
     * @param userVO 用户对象
     */
    MyResponse loginByEmail(UserVO userVO);

}
