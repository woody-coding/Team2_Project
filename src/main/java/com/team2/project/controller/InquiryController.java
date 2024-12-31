package com.team2.project.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team2.project.model.Inquiry;
import com.team2.project.model.InquiryCategory;
import com.team2.project.model.InquiryStatus;
import com.team2.project.model.Member;
import com.team2.project.service.InquiryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/inquiry")
@SessionAttributes("loginOK")
public class InquiryController {
	
	private final InquiryService inquiryService;
	
    // 1. 문의 조회 카테고리 버튼을 클릭했을 때 -> 조회 페이지로 이동 (날짜로 검색 x)
    @GetMapping
    public String findAllList(
    		@SessionAttribute("loginOK") Member member,
    		@RequestParam(required = false, defaultValue = "ALL") String status,
    		Model model) {
    	
    	 List<Inquiry> inquirys;
    	 
    	switch(status) {
    	case "ALL":
    		inquirys = inquiryService.findInquiry(member.getMemberNo());
    		break;
    	
    	case "PROCESSING":
    		inquirys = inquiryService.findInquiryByStatus(member.getMemberNo(), InquiryStatus.ANSWER_PROCESSING);
    		break;
    		
    	case "CHOSEN":
    		inquirys = inquiryService.findInquiryByStatus(member.getMemberNo(), InquiryStatus.ANSWER_CHOSEN);
    		break;
    	
    	default:
    		inquirys = inquiryService.findInquiry(member.getMemberNo());
    		break;
    	}
    	
        model.addAttribute("inquirys", inquirys);
        return "inquiry/list";
    }
    
    
    // 2. 조회 페이지에서 날짜 범위 선택 후, '조회' 버튼 눌렀을 때
    @GetMapping("/findByDate")
    public String findAllListByDate(
    		@SessionAttribute("loginOK") Member member, 
    		@RequestParam LocalDate startDate,
    		@RequestParam LocalDate endDate,
    		Model model) {
    	
        List<Inquiry> inquirys = inquiryService.findInquiryByDate(member.getMemberNo(), startDate, endDate);
        model.addAttribute("inquirys", inquirys);
        return "redirect:/inquiry";
    }
    
    
    // 3. 문의 접수 카테고리 버튼을 클릭했을 때 -> 접수 페이지로 이동
    @GetMapping("/add")
    public String moveToAddForm(
    		@SessionAttribute("loginOK") Member member) {
        return "inquiry/add";
    }
    
    
    // 4. 문의 접수 페이지에서, 접수하기 버튼을 클릭했을 때 -> 접수 완료 -> 조회 페이지로 리디렉션
    @PostMapping("/add")
    public String addInquiry(
    		@SessionAttribute("loginOK") Member member,
    		@RequestParam InquiryCategory inquiryCategory,
			@RequestParam String inquiryTitle,
			@RequestParam String inquiryContent,
			@RequestParam List<MultipartFile> files,
			@RequestParam(required = false) Integer inquiryNo) {
    	inquiryService.saveInquiry(member, inquiryCategory, inquiryTitle, inquiryContent, files, inquiryNo);
    	
        return "redirect:/inquiry";

    }
    
    
    // 5. 문의 조회 페이지에서, 수정 버튼을 눌렀을 때 -> 수정 페이지로 이동
    @GetMapping("/edit/{inquiryNo}")
    public String moveToEditForm(
    		@SessionAttribute("loginOK") Member member,
    		@PathVariable int inquiryNo,
    		Model model) {
    	
    	try {
            Inquiry inquiry = inquiryService.findById(inquiryNo);
            
            // 현재 로그인한 사용자가 해당 문의의 작성자인지 확인
            if (inquiry.getMember().getMemberNo() != member.getMemberNo()) {
                throw new IllegalArgumentException("회원이 일치하지 않습니다.");
            }
            
            model.addAttribute("inquiry", inquiry);
            return "inquiry/edit";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "inquiry/error";
        }
    }
    
    
    // 6. 문의 수정 페이지에서, 수정 후 접수하기 버튼을 클릭했을 때 -> 수정 완료 -> 조회 페이지로 리디렉션
    @PostMapping("/edit")
    public String editInquiry(
            @SessionAttribute("loginOK") Member member,
            @RequestParam InquiryCategory inquiryCategory,
            @RequestParam String inquiryTitle,
            @RequestParam String inquiryContent,
            @RequestParam List<MultipartFile> files,
            @RequestParam Integer inquiryNo,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            inquiryService.saveInquiry(member, inquiryCategory, inquiryTitle, inquiryContent, files, inquiryNo);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            redirectAttributes.addAttribute("inquiryNo", inquiryNo);
            return "redirect:/inquiry/edit/{inquiryNo}"; // 에러 메시지를 가지고 수정 페이지로 돌아감
        }

        return "redirect:/inquiry";
    }

			

	// 7. 문의 조회 페이지에서, 삭제 버튼을 눌렀을 때 -> 조회 페이지로 리디렉션
	@PostMapping("/delete/{inquiryNo}")
	public String deleteInquiry(
			@PathVariable int inquiryNo,
			@SessionAttribute("loginOK") Member member) {
		
		inquiryService.deleteInquiry(member.getMemberNo(), inquiryNo);
		
		return "redirect:/inquiry";
	}
	

	// 8. 관리자 페이지에서 해당 문의에 답변 후 '등록'버튼 클릭했을 때
	@PostMapping("/admin")
	public String addInquiryAnswerByAdmin(
			@PathVariable int inquiryNo,
			@RequestParam String inquiryAnswer,
			Model model) {
		
		// ** 관리자인지 아닌지 확인 로직 필요
	
    	inquiryService.updateInquiryByAdmin(inquiryNo, inquiryAnswer);
		return "redirect:/admin";
	}
			
}