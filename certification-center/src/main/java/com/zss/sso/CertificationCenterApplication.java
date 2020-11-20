package com.zss.sso;

import com.zss.sso.properties.LettuceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/19 16:40
 * @desc 认证中心启动类
 */
@SpringBootApplication
@EnableConfigurationProperties(LettuceProperties.class)
public class CertificationCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertificationCenterApplication.class, args);
    }
}
