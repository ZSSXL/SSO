package com.zss.base.comment;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 13:18
 * @desc 常量类
 */
public class Constant {

    /**
     * redis插入数据成功
     */
    public static final String SET_OK = "OK";

    /**
     * Redis有效期 - 30分钟（单位秒）
     */
    public static final long EFFECTIVE_TIME = 60L * 30L;

    /**
     * 认证
     */
    public static final String TICKET = "sso_ticket";
}
