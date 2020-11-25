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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 9:40
 * @desc 用户认证控制器
 * todo 解决跨域认证问题 - 如何证明我是我
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
     * @param login   登录信息传输对象
     * @param request request
     * @return 登录是否成功，成功则返回重定向地址
     */
    @PostMapping("/login")
    public ServerResponse<String> login(@RequestBody LoginDTO login,
                                        HttpServletRequest request) {
        // 1. 校验用户
        UserDTO userDTO = userService.checkUser(login);
        // 2. 生成ticket or token
        String ticket = request.getSession().getId();
        // 3. 保存用户信息SessionGeneralConverter
        String userSerializable = SerializableUtil.serializable(userDTO);

        String setResult = redisUtil.set(ticket, userSerializable, Constant.EFFECTIVE_TIME);
        // 4. 返回重定向地址和证书 - url & ticket
        if (Constant.SET_OK.equals(setResult)) {
            return ServerResponse.createBySuccess(ticket);
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
    public ServerResponse<String> certificate(@RequestBody String ticket) {
        if (StringUtils.isEmpty(ticket)) {
            return ServerResponse.createByError();
        }
        String result = ticket.replaceAll("\"", "");
        String userInfo = redisUtil.get(result);
        if (StringUtils.isNotEmpty(userInfo)) {
            // 反序列化
            UserDTO userDto = (UserDTO) SerializableUtil.serializeToObject(userInfo);
            log.info("UserInfo: [{}]", userDto);
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 测试
     *
     * @param content content
     * @param request request
     * @return anything
     */
    @Deprecated
    @PostMapping("/test")
    public ServerResponse<String> test(@RequestBody String content, HttpServletRequest request) {
        String cookie = request.getHeader("Cookie");
        System.out.println("=======================================");
        System.out.println("Cookie From Header: " + cookie);
        System.out.println("Session: " + request.getSession().getId());
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie item : cookies) {
                if ("sso_token".equals(item.getName())) {
                    System.out.println("SSO_TOKEN: " + item.getValue());
                }
                if ("sso".equals(item.getName())) {
                    System.out.println("sso: " + item.getValue());
                }
            }
        }
        System.out.println("Content: [" + content + "]");
        System.out.println("=======================================");
        return ServerResponse.createBySuccess(content);
    }
}
