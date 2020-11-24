package com.zss.sso.controller;

import com.zss.base.comment.Constant;
import com.zss.base.response.ServerResponse;
import com.zss.sso.entity.dto.LoginDTO;
import com.zss.sso.entity.dto.UserDTO;
import com.zss.sso.service.UserService;
import com.zss.sso.util.RedisUtil;
import com.zss.sso.util.SerializableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 9:40
 * @desc 用户认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/certificate")
public class CertificationController extends BaseController {

    private final UserService userService;
    private final RedisUtil redisUtil;

    @Autowired
    public CertificationController(UserService userService, RedisUtil redisUtil) {
        this.userService = userService;
        this.redisUtil = redisUtil;
    }

    /**
     * 用户登录
     *
     * @param login    登录信息传输对象
     * @param redirect 重定向地址
     * @return 登录是否成功，成功则返回重定向地址
     */
    @PostMapping("/login/{redirect}")
    public ServerResponse<Map<String, String>> login(@RequestBody LoginDTO login, @PathVariable("redirect") String redirect, HttpServletRequest request) {
        // 1. 校验用户
        UserDTO userDTO = userService.checkUser(login);
        // 2. 生成ticket or token
        String ticket = request.getSession().getId();
        // 3. 保存用户信息SessionGeneralConverter
        String userSerializable = SerializableUtil.serializable(userDTO);
        // 4. 获取发送请求的客户端主机的IP
        String remoteAddr = request.getRemoteAddr();
        System.out.println("RemoteAddr IP: [" + remoteAddr + "]");

        String setResult = redisUtil.set(ticket, userSerializable, Constant.EFFECTIVE_TIME);
        // 4. 返回重定向地址和证书 - url & ticket
        if (Constant.SET_OK.equals(setResult)) {
            Map<String, String> map = new HashMap<>(2);
            map.put("ticket", ticket);
            map.put("redirect", redirect);
            return ServerResponse.createBySuccess(map);
        } else {
            return ServerResponse.createByErrorMessage("用户名或者密码错误, 请重新尝试!");
        }
    }

    /**
     * ticket - 证书校验
     * 1. 如果不为空，则校验用户session是否存在
     * 2. 如果为空：
     * ---- （1）首先查看用户是否在已经登录
     * ---- （2）重定向到登录页面
     *
     * @param ticket 用户证书
     * @return 是否校验成功
     */
    @PostMapping
    public ServerResponse<UserDTO> certificate(@RequestBody String ticket, HttpServletResponse response, HttpServletRequest request) {
        String userInfo = redisUtil.get(ticket);
        if (StringUtils.isNotEmpty(userInfo)) {
            UserDTO userDTO = (UserDTO) SerializableUtil.serializeToObject(userInfo);
            System.out.println("User: [" + userDTO + "]");
            return ServerResponse.createBySuccess(userDTO);
        } else {
            // 重定向到用户登录界面
            try {
                response.sendRedirect("http://localhost:8880/index.html");
            } catch (IOException e) {
                log.warn("重定向失败:[{}]", e.getMessage());
            }
            return ServerResponse.createByErrorMessage("该用户未登录");
        }
    }

    /**
     * 测试
     *
     * @param content content
     * @param request request
     * @return anything
     */
    @PostMapping("/test")
    public ServerResponse<String> test(@RequestBody String content, HttpServletRequest request) {
        String cookie = request.getHeader("Cookie");
        System.out.println("=======================================");
        System.out.println("Cookie: " + cookie);
        System.out.println("Session: " + request.getSession().getId());
        System.out.println("Content: [" + content + "]");
        System.out.println("=======================================");
        return ServerResponse.createBySuccess(content);
    }
}
