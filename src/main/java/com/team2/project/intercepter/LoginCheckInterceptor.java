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
            // 로그인되지 않은 경우
            response.sendRedirect("/"); // 로그인 페이지로 리다이렉트
            return false; // 요청 중단
        }

        // 로그인된 경우
        return true; // 요청 계속 진행
    }
}
