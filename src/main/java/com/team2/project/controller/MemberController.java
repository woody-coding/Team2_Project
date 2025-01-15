package com.team2.project.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
		mav.setViewName("redirect:/");
		return mav;
	}
	@GetMapping(value="/login")
	public ModelAndView memberlogin() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login_join/loginMain");
		return mav;
	}
	@RequestMapping(value="join")
	public ModelAndView join() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login_join/join");
		return mav;
	}
	@RequestMapping(value="findID")
	public ModelAndView findID() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login_join/findID");
		return mav;
	}
	@RequestMapping(value="findPW")
	public ModelAndView findPW() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login_join/findPW");
		return mav;
	}
	@RequestMapping(value="/phonechk")
	public ModelAndView phonenumchk() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login_join/phonechk");
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
			mav.addObject("result","fail");
			mav.setViewName("redirect:/login");			
			return mav;
		}
		else {
			System.out.println("로그인 완료");
			//System.out.println(dto.getID());
			session.setAttribute("login", Memberlogin);
			String prevPage = (String) session.getAttribute("prevPage");
	        session.removeAttribute("prevPage");
	        String moveToUrl = prevPage != null ? "redirect:/" + prevPage : "redirect:/";
	        mav.addObject("result","sucess");
	        mav.setViewName(moveToUrl);
			mav.addObject(Memberlogin);
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
	
	@PostMapping("idchk")
	public boolean idchk(String id) {
		Optional<Member> idchk = memberService.idchk(id);
		if(idchk.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
	
	@PostMapping("/logout")
    public ModelAndView logout(HttpSession session) {
		ModelAndView mav = new ModelAndView();
        session.invalidate();
        
        mav.setViewName("redirect:/");
        return mav;
    }
}	
