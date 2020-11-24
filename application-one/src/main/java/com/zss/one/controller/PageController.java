package com.zss.one.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/24 14:21
 * @desc 页面控制器
 */
@Controller
public class PageController extends BaseController {

    /**
     * 页面跳转
     *
     * @param page page
     * @return String
     */
    @GetMapping("/{page}")
    public String helloHtml(@PathVariable String page) {
        return page;
    }
}
