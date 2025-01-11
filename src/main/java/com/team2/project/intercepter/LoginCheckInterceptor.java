package com.team2.project.intercepter;


import org.springframework.web.servlet.HandlerInterceptor;

import com.team2.project.model.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Optional<Member> loginMember = (Optional<Member>) session.getAttribute("login");

        if (loginMember == null || loginMember.isEmpty()) {
        	String requestURI = request.getRequestURI();
        	if (requestURI.startsWith("/")) {
        	    requestURI = requestURI.substring(1);
        	}

            String queryString = request.getQueryString();
            if (queryString != null) {
                requestURI += "?" + queryString;
            }
            
            session.setAttribute("prevPage", requestURI);
            response.sendRedirect("/"); // 로그인 페이지로 리다이렉트
            return false;
        }
        return true;
    }
}