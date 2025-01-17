package com.team2.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.team2.project.model.Inquiry;
import com.team2.project.model.Member;
import com.team2.project.service.InquiryService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/inquiry")
public class AdminForInquiryController {

	@Autowired
	private InquiryService inquiryService;

	// 문의 리스트
	@GetMapping("/inquiryList")
	public String showList(@SessionAttribute("login") Optional<Member> optionalMember, Model model, 
	                       @RequestParam(defaultValue = "0") int page,
	                       @RequestParam(defaultValue = "20") int size) {

	        List<Inquiry> allInquirys = inquiryService.findAllInquiry();
	        int totalInquirys = allInquirys != null ? allInquirys.size() : 0;
	        int totalPages = (int) Math.ceil((double) totalInquirys / size);

	        int start = page * size;
	        int end = Math.min(start + size, totalInquirys);

	        List<Inquiry> inquirys = allInquirys.subList(start, end);

	        model.addAttribute("inquirys", inquirys);
	        model.addAttribute("currentPage", page);
	        model.addAttribute("totalPages", totalPages);
	        model.addAttribute("totalInquirys", totalInquirys);
	        model.addAttribute("allInquirys", allInquirys);

	    return "admin/adminInquiryList";
	}


	// 문의 답변
	@PostMapping("/answer")
	public String updateInquiryAnswerByAdmin(
			@SessionAttribute("login") Optional<Member> optionalMember,
			@RequestParam int inquiryNo, 
			@RequestParam String inquiryAnswer) {
		inquiryService.updateInquiryAnswerByAdmin(inquiryNo, inquiryAnswer);
		
		return "redirect:/admin/inquiry/inquiryList";
	}
	
	
	// 문의 삭제
	@PostMapping("/delete/{inquiryNo}")
	public String deleteInquiry(
			@PathVariable String inquiryNo,
			@SessionAttribute("login") Optional<Member> optionalMember) {
		
		int inquiryNum = Integer.parseInt(inquiryNo);
		
		inquiryService.deleteByInquiryId(inquiryNum);
		
		return "redirect:/admin/inquiry/inquiryList";
	}


}
