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
        List<User> users = userRepository.findAll();
        try {
            User user = UserMapper.INSTANCE.v2p(userVO);
            for (User u : users) {
                if (u.getUsername().equals(user.getUsername()))
                    return MyResponse.buildFailure("用户已存在!");
                if (user.getEmail()!=null){
                    if (u.getEmail()!=null){
                        if (u.getEmail().equals(user.getEmail()))
                            return MyResponse.buildFailure("邮箱已存在!");
                    }
                }
            }
            return MyResponse.buildSuccess(userRepository.save(user));
        } catch (Exception e) {
            e.printStackTrace();
            return MyResponse.buildFailure(e.getLocalizedMessage());
        }
    }

    @Override
    public MyResponse loginByUsername(UserVO userVO) {
        User user = UserMapper.INSTANCE.v2p(userVO);
        User u = userRepository.getUserByUsername(userVO.getUsername());

        if (!user.getPassword().equals(u.getPassword()))
            return MyResponse.buildFailure("用户名或密码错误!");
        else
            return MyResponse.buildSuccess(UserMapper.INSTANCE.p2v(u));
    }

    @Override
    public MyResponse loginByEmail(UserVO userVO) {
        MyResponse response = new MyResponse();
        response.setIsSuccess(true);
        User user = UserMapper.INSTANCE.v2p(userVO);
        User u = userRepository.getUserByEmail(userVO.getEmail());

        if (!user.getPassword().equals(u.getPassword()))
            return MyResponse.buildFailure("用户名或邮箱错误!");
        else return MyResponse.buildSuccess(UserMapper.INSTANCE.p2v(u));

    }


}
