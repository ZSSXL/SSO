package com.zss.sso.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/7/15 13:24
 * @desc dozer - 配置类
 */
@Configuration
public class DozerBeanMapperConfig {

    @Bean
    public DozerBeanMapper mapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.setMappingFiles(Collections.singletonList("config/dozer-conf.xml"));
        return mapper;
    }
}
