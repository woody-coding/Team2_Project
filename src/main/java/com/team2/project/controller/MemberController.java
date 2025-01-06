package com.team2.project.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kakao.app.KakaoAPI;
import com.team2.project.DTO.FindDTO;
import com.team2.project.DTO.LoginDTO;
import com.team2.project.model.Member;
import com.team2.project.repository.MemberRepository;
import com.team2.project.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping()
@Controller
@RequiredArgsConstructor
public class MemberController {
	
	
	@Autowired
	private MemberRepository memberRepo;
	
	private final MemberService memberService;
	
	KakaoAPI kakaoApi = new KakaoAPI();
	
	@GetMapping(value="/kakaologin")
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
		dto.setMemberStatus("MEMBER");
		dto.setMemberDate(new Date());
		memberRepo.save(dto);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/");
		return mav;
	}
	@PostMapping("login")
	public ModelAndView login(LoginDTO dto, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		Optional<Member> Memberlogin = memberService.getMemberLoginCheck(dto);
		if(Memberlogin.isEmpty()) {
			mav.setViewName("redirect:/");
			return mav;
		}
		else {
			System.out.println("로그인 완료");
			//System.out.println(dto.getID());
			session.setAttribute("login", Memberlogin);
			return mav;
		}
	}
	@PostMapping("searchId")
	public String findId(FindDTO dto) {
		//ModelAndView mav = new ModelAndView();
		Optional<Member> findId = memberService.FindId(dto);
		if(findId.isEmpty()) {
			//mav.setViewName("redirect:/findID");
			return "조회된 ID가 없습니다.";
		}
		else {
			System.out.println("조회 완료");
			System.out.println(findId);
			return "조회된 ID : "+findId.get().getMemberId();
		}
	}
	@PostMapping("searchIdPW")
	public String findIdPW(FindDTO dto) {
		//ModelAndView mav = new ModelAndView();
		Optional<Member> findIdPw = memberService.FindIdPw(dto);
		if(findIdPw.isEmpty()) {
			//mav.setViewName("redirect:/findPW");
			return "조회된 PW가 없습니다.";
		}
		else {
			System.out.println("조회 완료");
			System.out.println(findIdPw);
			return 	"조회한 ID : "+findIdPw.get().getMemberId()+"\n"  
					+ "조회된 PW : "+findIdPw.get().getMemberPw();
		}
	}
}	
