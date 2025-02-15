package com.team2.project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.team2.project.intercepter.AdminCheckInterceptor;
import com.team2.project.intercepter.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		
		// 로그인 인터셉터
	    registry.addInterceptor(new LoginCheckInterceptor())
	            .addPathPatterns("/**")
	            .excludePathPatterns("", "/", "/login", "/join", "/css/**", "/js/**", "/images/**", "/imgs/**", "/kakaologin", "/findID",
	                                "/findPW", "/phonechk", "/insert", "/searchId", "/searchIdPW", "/idchk", "/show/ticketMenu", "/show/ticketMenu/{showCate}",
	                                "/show/ticketDetailPage/{showNo}", "/show/Main", "/actor/{actorNo}", "/show/search", "/show/search/**", "/finalFile/**");
	
	
		// 관리자 인터셉터
	    registry.addInterceptor(new AdminCheckInterceptor())
        .addPathPatterns("/admin/**")
	    .excludePathPatterns("/admin/finalFile/**");
	
	}


    // * 이미지 파일 업로드 경로를	>>	C:/finalFile/ 폴더로 경로로 설정할 때
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/finalFile/**")
                .addResourceLocations("file:C:/finalFile/");
    }
}
