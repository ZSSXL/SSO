package com.zss.one.controller;

import com.zss.base.response.ServerResponse;
import com.zss.one.service.AppOneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * http测试
     *
     * @param name  名称
     * @param year  年份
     * @param month 月份
     * @param day   日期
     * @return anything
     */
    @GetMapping("/{name}")
    public ServerResponse<String> httpTest(@PathVariable("name") String name,
                                           @RequestParam String year,
                                           @RequestParam String month,
                                           @RequestParam String day) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name=").append(name).append(",")
                .append("Year=").append(year).append(",")
                .append("Month=").append(month).append(",")
                .append("Day=").append(day);
        System.out.println("Result: " + sb.toString());
        return ServerResponse.createBySuccess(sb.toString());
    }
}
