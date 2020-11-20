package com.zss.sso;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 10:23
 * @desc 测试基类
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseTest {

    @Before
    public void before() {
        System.out.println("===================== 测试开始 =====================");
    }

    @After
    public void after() {
        System.out.println("===================== 测试结束 =====================");
    }
}
