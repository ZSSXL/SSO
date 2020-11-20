package com.zss.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 16:12
 * @desc 页面管理
 * 【注意】此处要使用 @Controller 而不是 @RestController
 */
@Controller
public class HtmlController {

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
