package com.zss.sso.service.impl;

import com.zss.base.util.EncryptionUtil;
import com.zss.sso.entity.User;
import com.zss.sso.entity.dto.LoginDTO;
import com.zss.sso.entity.dto.UserDTO;
import com.zss.sso.repository.UserRepository;
import com.zss.sso.service.UserService;
import com.zss.sso.util.GeneralConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/19 17:24
 * @desc 用户服务层接口方法实现
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GeneralConverter generalConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, GeneralConverter generalConverter) {
        this.userRepository = userRepository;
        this.generalConverter = generalConverter;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDTO checkUser(LoginDTO login) {
        Optional<User> optional = userRepository.findByUsernameAndPassword(login.getUsername(),
                EncryptionUtil.encrypt(login.getPassword()));
        User user = optional.orElse(null);
        return generalConverter.converter(user, UserDTO.class);
    }
}
