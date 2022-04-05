package com.example.deepname.Service.Impl;

import com.example.deepname.Entity.User;
import com.example.deepname.Repository.UserRepository;
import com.example.deepname.Service.UserService;
import com.example.deepname.Utils.MyResponse;
import com.example.deepname.Utils.VPMapper.UserMapper;
import com.example.deepname.VO.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;

    @Override
    public MyResponse register(UserVO userVO) {
        MyResponse response = new MyResponse();
        response.setIsSuccess(true);
        List<User> users = userRepository.findAll();
        try {
            User user = UserMapper.INSTANCE.v2p(userVO);
            for (User u : users) {
                if (u.getUsername().equals(user.getUsername()) || u.getEmail().equals(user.getEmail())) {
                    response.setMsg("用户已存在!");
                    response.setIsSuccess(false);
                }
            }
            if (response.getIsSuccess()) response.setData(userRepository.save(user));
        } catch (Exception e){
            response.setIsSuccess(false);
            response.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public MyResponse loginByUsername(UserVO userVO) {
        MyResponse response = new MyResponse();
        response.setIsSuccess(true);
        User user = UserMapper.INSTANCE.v2p(userVO);
        User u = userRepository.getUserByUsername(userVO.getUsername());

        if (!user.getPassword().equals(u.getPassword())) {
            response.setIsSuccess(false);
            response.setMsg("用户名或密码错误!");
        } else response.setData(UserMapper.INSTANCE.p2v(u));

        return response;
    }

    @Override
    public MyResponse loginByEmail(UserVO userVO) {
        MyResponse response = new MyResponse();
        response.setIsSuccess(true);
        User user = UserMapper.INSTANCE.v2p(userVO);
        User u = userRepository.getUserByEmail(userVO.getEmail());

        if (!user.getPassword().equals(u.getPassword())) {
            response.setIsSuccess(false);
            response.setMsg("用户邮箱或密码错误!");
        } else response.setData(UserMapper.INSTANCE.p2v(u));

        return response;
    }


}
