package com.zss.sso;

import java.text.SimpleDateFormat;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/26 14:51
 * @desc
 */
public class Test {
    private SimpleDateFormat simpleDateFormat;

    public static void main(String[] args) {
        Test test = new Test();
        test.test();
    }

    public void test() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    }
}
