package com.zss.sso.repository;

import com.zss.sso.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/19 17:20
 * @desc 用户持久化
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 用户校验
     *
     * @param username 用户名
     * @param password 用户密码
     * @return User
     */
    Optional<User> findByUsernameAndPassword(String username, String password);
}
