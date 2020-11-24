package com.zss.two.controller;

import com.zss.base.annotation.RequiredPermission;
import com.zss.base.response.ServerResponse;
import com.zss.two.service.AppTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 16:58
 * @desc APP TWO Controller
 */
@RestController
@RequestMapping("/two")
public class AppTwoController extends BaseController {

    private final AppTwoService appTwoService;

    @Autowired
    public AppTwoController(AppTwoService appTwoService) {
        this.appTwoService = appTwoService;
    }

    @PostMapping
    @RequiredPermission
    public ServerResponse<String> appTwo(@RequestBody String content, HttpServletRequest request) {
        String result = appTwoService.appTwo(content);
        System.out.println("SessionId: [" + request.getSession().getId() + "]");
        return ServerResponse.createBySuccess(result);
    }
}
