package com.team2.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.team2.project.model.Seat;
import com.team2.project.service.ShowMainService;

@Controller
@RequestMapping("/show")
public class ShowController {
	
	@Autowired
	private ShowMainService showService;
	
	@GetMapping("/Main/{memberNo}")
	public String goShowMain(@PathVariable int memberNo, Model model) {
		
		showService.goShowMain(memberNo, model);
		
		return "actorInfo_showMain/showMain";
	}

	@GetMapping("/bookSeat/{showNo}/{memberNo}/{date}")
	public String BookSeat(@PathVariable int showNo, @PathVariable int memberNo, @PathVariable String date, Model model) {
		
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
