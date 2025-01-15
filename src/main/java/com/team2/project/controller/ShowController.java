package com.team2.project.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.team2.project.model.Member;
import com.team2.project.model.Seat;
import com.team2.project.service.ShowMainService;

@Controller
@RequestMapping("/show")
public class ShowController {
	
	@Autowired
	private ShowMainService showService;
	
	@GetMapping("/Main")
	public String goShowMain(Model model) {
		
		showService.goShowMain(model);
		
		return "actorInfo_showMain/showMain";
	}

	@GetMapping("/bookSeat/{showNo}/{date}")
	public String BookSeat(@PathVariable int showNo, @PathVariable String date, @SessionAttribute("login") Optional<Member> optionalMember, Model model) {

		Member member = optionalMember.orElseThrow(() -> new IllegalStateException("로그인이 필요합니다."));
		
		int memberNo = member.getMemberNo();
		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(memberNo);
		
		showService.bookSeat(showNo, memberNo, date, model);

		return "ticket/seatSelection/seatSelection";
	}
	
	@GetMapping("/seatInfo")
	@ResponseBody
	public List<Seat> getSeatInfo(@RequestParam String showNo, @RequestParam String showDate){
		List<Seat> allSeatList = showService.allSeatList(showNo, showDate);
		
		return allSeatList;
	}
}
