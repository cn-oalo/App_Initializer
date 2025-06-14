package cn.oalo.boot.config;

import cn.oalo.infrastructure.interceptor.UserContextInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    
    private final UserContextInterceptor userContextInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册用户上下文拦截器
        registry.addInterceptor(userContextInterceptor).addPathPatterns("/**");
    }
} 