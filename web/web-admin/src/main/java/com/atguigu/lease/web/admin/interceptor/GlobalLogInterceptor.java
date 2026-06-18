/**
 * @author 23275
 * @version 1.0
 * @since 2026/5/4
 */
package com.atguigu.lease.web.admin.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

// 打印全局日志
@Slf4j
@Component
public class GlobalLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求的方法 (GET, POST, etc.)
        String method = request.getMethod();

        // 2. 获取实际调用的 URL 路径 (例如: /api/v1/users)
        String uri = request.getRequestURI();

        // 3. 获取 Spring 映射的 Pattern 路径 (例如: /api/v1/users/{id})
        // 这一步很重要，因为它能告诉你匹配的是哪个定义的接口规则，而不是具体的参数值
        String bestPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        // 打印日志
        if (bestPattern != null) {
            log.info("⚡️ 请求拦截 -> 方法: {}, 实际路径: {}, 映射规则: {}", method, uri, bestPattern);
        } else {
            log.info("⚡️ 请求拦截 -> 方法: {}, 实际路径: {}", method, uri);
        }

        // 返回 true 表示继续执行后续的 Controller 方法
        // 返回 false 则会中断请求
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
