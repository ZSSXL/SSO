package com.zss.sso;

import com.zss.sso.util.RedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 10:39
 * @desc Redis测试
 */
public class RedisTest extends BaseTest {

    @Autowired
    private RedisUtil redisUtil;

    private final String key = "hello!";

    @Test
    public void setTest() {
        String value = "hi!";

        String result = redisUtil.set(key, value);
        System.out.println("Result: " + result);
    }

    @Test
    public void getTest() {
        String value = redisUtil.get(key);
        System.out.println("Value: " + value);
    }

}
