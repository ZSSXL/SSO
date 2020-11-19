package com.zss.sso.repository;

import com.zss.sso.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/19 17:20
 * @desc 用户持久化
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
