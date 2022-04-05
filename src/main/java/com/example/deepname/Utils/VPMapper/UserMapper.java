package com.example.deepname.Utils.VPMapper;

import com.example.deepname.Entity.User;
import com.example.deepname.VO.UserVO;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({})
    UserVO p2v(User user);
    List<UserVO> pList2vList(List<User> userList);

    @Mappings({})
    User v2p(UserVO userVO);
    List<User> vList2pList(List<UserVO> penaltyVOList);
}
