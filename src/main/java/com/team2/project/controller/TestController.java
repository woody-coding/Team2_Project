package com.team2.project.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kakao.app.KakaoAPI;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping()
public class TestController {
	
	KakaoAPI kakaoApi = new KakaoAPI();
	
	@GetMapping(value="/login")
	public ModelAndView login(@RequestParam("code") String code, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		// 1번 인증코드 요청 전달
		String accessToken = kakaoApi.getAccessToken(code);
		// 2번 인증코드로 토큰 전달
		HashMap<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);
		
		System.out.println("login info : " + userInfo.toString());
		mav.addObject("userId");
		mav.setViewName("index");
		return mav;
	}
	
	
	@RequestMapping(value="/join")
	public ModelAndView join() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("join");
		return mav;
	}
	
	@RequestMapping(value="/findID")
	public ModelAndView findID() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("findID");
		return mav;
	}
	@RequestMapping(value="/findPW")
	public ModelAndView findPW() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("findPW");
		return mav;
	}
	@RequestMapping(value="/phonechk")
	public ModelAndView phonenumchk() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("phonechk");
		return mav;
	}
	
}
