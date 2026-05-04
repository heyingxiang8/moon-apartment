/**
 * @author 23275
 * @version 1.0
 * @since 2026/5/4
 */
package com.atguigu.lease.config;

import com.atguigu.lease.web.admin.custom.WebMvcConfiguration;
import com.atguigu.lease.web.admin.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class AdminWebMvcConfiguration extends WebMvcConfiguration {
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Value("${admin.auth.path-patterns.include}")
    private String[] includePathPatterns;

    @Value("${admin.auth.path-patterns.exclude}")
    private String[] excludePathPatterns;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.authenticationInterceptor)
                .addPathPatterns(includePathPatterns)
                .excludePathPatterns(excludePathPatterns);
    }
}
