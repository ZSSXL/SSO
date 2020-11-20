package com.zss.sso.service;

import com.zss.sso.entity.User;
import com.zss.sso.entity.dto.LoginDTO;
import com.zss.sso.entity.dto.UserDTO;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/19 17:23
 * @desc 用户服务层接口
 */
public interface UserService {

    /**
     * 创建用户
     *
     * @param user 用户相关信息
     * @return 创建完成的用户
     */
    User createUser(User user);

    /**
     * 登录校验
     *
     * @param login 登录信息
     * @return 用户信息
     */
    UserDTO checkUser(LoginDTO login);
}
