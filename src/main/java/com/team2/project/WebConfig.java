package com.team2.project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.team2.project.intercepter.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(new LoginCheckInterceptor())
	            .addPathPatterns("/**")
	            .excludePathPatterns("/", "/login", "/join", "/css/**", "/js/**", "/images/**", "/imgs/**", "/kakaologin", "/findID",
	                                "/findPW", "/phonechk", "/insert", "/searchId", "/searchIdPW", "/idchk", "/ticketMenu", "/ticketMenu/{showCate}",
	                                "/ticketDetailPage/{showNo}", "/show/Main", "/actor/{actorNo}");
	}

    // * 이미지 파일 업로드 경로를	>>	C:/finalFile/ 폴더로 경로로 설정할 때
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/finalFile/**")
//                .addResourceLocations("file:C:/finalFile/");
//    }
}
