package com.team2.project.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.team2.project.model.LikeYO;
import com.team2.project.model.Member;
import com.team2.project.service.ActorService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/actor")
public class ActorController {
	
	@Autowired
	private ActorService actorService;	
	
	@GetMapping("/{actorNo}")
	public String goActorInfo(@PathVariable int actorNo, HttpSession session, Model model) {
		
		Member member = (Member) session.getAttribute("login");
		
		int memberNo;
		
		if (member == null) {
	        memberNo = 0;
	    } else {
			memberNo = member.getMemberNo();
		}
		
		actorService.getActorInfo(actorNo, memberNo, model);
		
		return "actorInfo_showMain/actorInfo";
	}
	
	@PostMapping("/like-toggle")
	@ResponseBody
	public Map<String, String> toggleLike(@RequestBody LikeYO likeYo){
		//요청 데이터 확인하기
		
		System.out.println("-------------------------");
		System.out.println("");
		System.out.println("MemberNo: " + likeYo.getMemberNo());
		System.out.println("ActorNO: " + likeYo.getActorNo());
		System.out.println("ShowNo: " + likeYo.getShowNO());
        System.out.println("Status: " + likeYo.getStatus());
        
        boolean success = actorService.toggleLike(likeYo);
        
        Map<String, String> response = new HashMap<>();
        
        if (success) {
            response.put("message", "Like status updated successfully.");
        } else {
            response.put("message", "Failed to update like status.");
        }
        
        return response;
	}
	
}
