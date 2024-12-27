package com.team2.project.controller;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kakao.app.KakaoAPI;
import com.team2.project.model.Member;
import com.team2.project.repository.Memberdao;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping()
@Controller
public class MemberController {
	
	
	@Autowired
	private Memberdao dao;
	
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
	
	@PostMapping("insert")
	public ModelAndView insert(Member dto) {
		dto.setMemberStatus(true);
		dto.setMemberDate(new Date());
		dao.save(dto);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/");
		return mav;
	}	
}	
