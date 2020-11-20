package com.zss.two.service.impl;

import com.zss.two.service.AppTwoService;
import org.springframework.stereotype.Service;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 16:56
 * @desc 服务层接口的实现
 */
@Service
public class AppTwoServiceImpl implements AppTwoService {

    @Override
    public String appTwo(String content) {
        return content;
    }
}
