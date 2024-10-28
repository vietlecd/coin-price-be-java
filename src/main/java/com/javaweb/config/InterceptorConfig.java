package com.javaweb.config;


import com.javaweb.Interceptor.AuthenInterceptor;
import com.javaweb.Interceptor.Vip1Interceptor;
import com.javaweb.Interceptor.Vip2Interceptor;
import com.javaweb.Interceptor.Vip3Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private AuthenInterceptor authInterceptor;
    @Autowired
    private Vip1Interceptor vip1Interceptor;
    @Autowired
    private Vip2Interceptor vip2Interceptor;
    @Autowired
    private Vip3Interceptor vip3Interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/api/**");
        registry.addInterceptor(vip1Interceptor).addPathPatterns("/api/vip1/**");
        registry.addInterceptor(vip2Interceptor).addPathPatterns("/api/vip2/**");
        registry.addInterceptor(vip3Interceptor).addPathPatterns("/api/vip3/**");
    }
}
