package com.zss.one.controller;

import com.zss.base.response.ServerResponse;
import com.zss.one.service.AppOneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 16:58
 * @desc APP TWO Controller
 */
@RestController
@RequestMapping("/one")
public class AppOneController {

    private final AppOneService appOneService;

    @Autowired
    public AppOneController(AppOneService appOneService) {
        this.appOneService = appOneService;
    }

    @PostMapping
    public ServerResponse<String> appOne(@RequestBody String content) {
        String result = appOneService.appOne(content);
        return ServerResponse.createBySuccess(result);
    }
}
