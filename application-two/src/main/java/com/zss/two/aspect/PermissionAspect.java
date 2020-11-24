package com.zss.two.aspect;

import com.alibaba.fastjson.JSONObject;
import com.zss.base.response.ResponseCode;
import com.zss.base.response.ServerResponse;
import com.zss.base.util.HttpUtil;
import com.zss.base.util.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/23 17:04
 * @desc 用户请求拦截
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Around(value = "@annotation(com.zss.base.annotation.RequiredPermission)")
    public Object aroundPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            System.out.println("RequestURL: 【" + request.getRequestURL() + "】");
            System.out.println("APP ONE SessionID: " + request.getSession().getId());
            String uri = "http://localhost:8880/certificate/test";
            String content = "The world not enough";
            HashMap<String, String> cookiesMap = MapUtil.create(
                    "PING", "PONG",
                    "APP", "TWO");
            CloseableHttpResponse closeableHttpResponse = HttpUtil.doPost(uri, content, cookiesMap);
            if (closeableHttpResponse != null) {
                System.out.println("----------------------------------------");
                System.out.println("HttpStatus: " + closeableHttpResponse.getStatusLine());
                try {
                    HttpEntity entity = closeableHttpResponse.getEntity();
                    JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(entity));
                    String status = jsonObject.getString("status");
                    if (ResponseCode.SUCCESS.getCode() == Integer.parseInt(status)) {
                        // 通过
                        return joinPoint.proceed();
                    } else {
                        // 身份校验未通过
                        // 【注】本来想[response.sendRedirect()]重定向的，但是不知道怎么操作，
                        // 现在只能先返回特定信息，交给前端来判断了
                        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                                ResponseCode.ILLEGAL_ARGUMENT.getDesc());
                    }
                } catch (IOException e) {
                    log.error("出现未知异常: [{}]", e.getMessage());
                    return ServerResponse.createByErrorMessage("发生未知异常");
                }
            }
        }
        log.error("权限校验拦截失败");
        return ServerResponse.createByErrorMessage("请刷新重新尝试");
    }
}
