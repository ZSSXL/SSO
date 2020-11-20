package com.zss.two.controller;

import com.zss.base.response.ServerResponse;
import com.zss.two.service.AppTwoService;
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
@RequestMapping("/two")
public class AppTwoController {

    private final AppTwoService appTwoService;

    @Autowired
    public AppTwoController(AppTwoService appTwoService) {
        this.appTwoService = appTwoService;
    }

    @PostMapping
    public ServerResponse<String> appTwo(@RequestBody String content) {
        String result = appTwoService.appTwo(content);
        return ServerResponse.createBySuccess(result);
    }
}
