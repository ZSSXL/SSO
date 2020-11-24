package com.zss.one.controller;

import com.zss.base.annotation.RequiredPermission;
import com.zss.base.response.ServerResponse;
import com.zss.one.service.AppOneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 16:58
 * @desc APP TWO Controller
 */
@RestController
@RequestMapping("/one")
public class AppOneController extends BaseController {

    private final AppOneService appOneService;

    @Autowired
    public AppOneController(AppOneService appOneService) {
        this.appOneService = appOneService;
    }

    @PostMapping
    @RequiredPermission
    public ServerResponse<String> appOne(@RequestBody String content, HttpServletRequest request) {
        String result = appOneService.appOne(content);
        System.out.println("SessionId: [" + request.getSession().getId() + "]");
        return ServerResponse.createBySuccess(result);
    }

    /**
     * http测试
     *
     * @param name    名称
     * @param year    年份
     * @param month   月份
     * @param day     日期
     * @param request 请求
     * @return anything
     */
    @GetMapping("/{name}")
    public ServerResponse<String> httpTest(@PathVariable("name") String name,
                                           @RequestParam String year,
                                           @RequestParam String month,
                                           @RequestParam String day,
                                           HttpServletRequest request) {
        String cookie = request.getHeader("Cookie");
        System.out.println("Cookie: [" + cookie + "]");
        System.out.println("Session: [" + request.getSession().getId() + "]");
        System.out.println("=============================");
        StringBuilder sb = new StringBuilder();
        sb.append("Name=").append(name).append(",")
                .append("Year=").append(year).append(",")
                .append("Month=").append(month).append(",")
                .append("Day=").append(day);
        System.out.println("Result: " + sb.toString());
        return ServerResponse.createBySuccess(sb.toString());
    }
}
