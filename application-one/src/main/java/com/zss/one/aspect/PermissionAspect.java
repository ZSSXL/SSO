package com.zss.one.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/23 17:04
 * @desc 用户请求拦截
 */
@Slf4j
// @Aspect
@Component
public class PermissionAspect {

    // @Around(value = "@annotation(com.zss.base.annotation.RequiredPermission)")
    public Object aroundPermission(ProceedingJoinPoint joinPoint) throws Throwable {

        // 通过 joinPoint.proceed();
        return null;
    }
}
