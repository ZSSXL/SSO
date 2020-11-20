package com.zss.sso.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 9:43
 * @desc 登录信息传输对象
 */
@Data
public class LoginDTO implements Serializable {

    private String username;

    private String password;
}
