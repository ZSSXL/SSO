package com.zss.sso;

import com.zss.base.util.EncryptionUtil;
import com.zss.base.util.IdUtil;
import com.zss.sso.entity.User;
import com.zss.sso.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 10:25
 * @desc 用户测试
 */
public class UserTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void createUserTest() {
        User build = User.builder()
                .userId(IdUtil.getId())
                .username("sso")
                .password(EncryptionUtil.encrypt("sso_pass"))
                .age("24")
                .address("上海市浦东新区")
                .number("8008208820").build();

        User user = userService.createUser(build);
        System.out.println("User: " + user);
    }
}
