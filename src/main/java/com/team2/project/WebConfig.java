package com.team2.project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.team2.project.intercepter.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/**") // 모든 경로에 대해 인터셉터 적용
                .excludePathPatterns("/", "/login", "/join", "/css/**", "/js/**", "/images/**", "/imgs/**", "/kakaologin", "/findID",
                						"/findPW", "/phonechk", "/insert", "/searchId", "/searchIdPW", "/idchk"); // 제외할 경로 지정
    }
}
