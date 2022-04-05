package com.example.deepname.VO;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserVO {
    private Integer id; //用户主键

    @Size(max = 64, message = "用户名长度不能超过64")
    private String username;

    @Size(max = 64, min = 6, message = "密码长度不能超过64,不能低于6")
    private String password;

    @Size(max = 64, message = "邮箱长度不能超过64")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式不正确")
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
