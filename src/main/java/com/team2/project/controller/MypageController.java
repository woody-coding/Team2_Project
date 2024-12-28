package com.team2.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mypage")
public class MypageController {
	@GetMapping("/main")
	public String mainPage(Model model, @RequestParam int memberNo) {
		return "mypage/main";
	}
}
