package com.hls.logback.config;

import com.hls.logback.interceptor.CheckTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.annotation.Resource;
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private CheckTokenInterceptor checkTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkTokenInterceptor)
                .excludePathPatterns("/login/**")
                .excludePathPatterns("/test/**");
    }
}
