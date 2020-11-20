package com.zss.sso.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 10:47
 * @desc 用户信息数据传输对象
 */
@Data
public class UserDTO implements Serializable {

    private String userId;

    private String username;

    private String number;

    private String age;

    private String address;
}
