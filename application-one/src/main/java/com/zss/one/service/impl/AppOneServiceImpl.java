package com.zss.one.service.impl;

import com.zss.one.service.AppOneService;
import org.springframework.stereotype.Service;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 17:09
 * @desc 服务层接口方法实现
 */
@Service
public class AppOneServiceImpl implements AppOneService {

    @Override
    public String appOne(String content) {
        return content;
    }
}
