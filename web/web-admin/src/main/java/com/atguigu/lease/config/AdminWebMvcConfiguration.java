/**
 * @author 23275
 * @version 1.0
 * @since 2026/5/4
 */
package com.atguigu.lease.config;

import com.atguigu.lease.web.admin.interceptor.AuthenticationInterceptor;
import com.atguigu.lease.web.admin.interceptor.GlobalLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 配置拦截器
@Configuration
public class AdminWebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;
    @Autowired
    private GlobalLogInterceptor globalLogInterceptor;

    @Value("${admin.auth.path-patterns.include}")
    private String[] includePathPatterns;

    @Value("${admin.auth.path-patterns.exclude}")
    private String[] excludePathPatterns;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册日志记录拦截器，拦截所有请求，优先级为1
        registry.addInterceptor(this.globalLogInterceptor)
                .addPathPatterns(includePathPatterns)
                .order(1);
        // 注册登录认证拦截器，设置拦截路径和排除路径，优先级为2
        registry.addInterceptor(this.authenticationInterceptor)
                .addPathPatterns(includePathPatterns)
                .excludePathPatterns(excludePathPatterns)
                .order(2);
    }
}
