package com.example.deepname.Repository;

import com.example.deepname.Entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * 根据用户名查找账号
     */
    User getUserByUsername(String username);

    /**
     * 根据用户邮箱查找账号
     */
    User getUserByEmail(String email);
}
