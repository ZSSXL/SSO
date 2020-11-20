package com.zss.sso;

import com.zss.base.util.EncryptionUtil;
import com.zss.base.util.IdUtil;
import com.zss.sso.entity.User;
import com.zss.sso.util.SerializableUtil;
import org.junit.Test;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 13:03
 * @desc 对象序列化测试
 */
public class SerializableTest {

    @Test
    public void serializableTest() {
        User build = User.builder()
                .userId(IdUtil.getId())
                .username("sso")
                .password(EncryptionUtil.encrypt("sso_pass"))
                .age("24")
                .address("上海市浦东新区")
                .number("8008208820").build();
        // 对象序列化
        String serializable = SerializableUtil.serializable(build);
        System.out.println("[ " + serializable + " ]");
        User user = null;
        if (serializable != null) {
            // 对象反序列化
            user = (User) SerializableUtil.serializeToObject(serializable);
        }
        System.out.println("User: " + user);
    }

}
