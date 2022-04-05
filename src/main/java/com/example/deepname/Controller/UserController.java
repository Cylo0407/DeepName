package com.example.deepname.Controller;

import com.example.deepname.Service.UserService;
import com.example.deepname.Utils.MyResponse;
import com.example.deepname.VO.UserVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public MyResponse register(@Valid @RequestBody UserVO userVO){
        return userService.register(userVO);
    }


    /**
     * 用户名登录
     */
    @PostMapping("/loginByUsername")
    public MyResponse loginByUsername(@Valid @RequestBody UserVO userVO){
        return userService.loginByUsername(userVO);
    }

    /**
     * 用户邮箱登录
     */
    @PostMapping("/loginByEmail")
    public MyResponse loginByEmail(@Valid @RequestBody UserVO userVO){
        return userService.loginByEmail(userVO);
    }

}