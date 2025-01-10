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
import com.team2.project.service.ActorService;

@Controller
@RequestMapping("/actor")
public class ActorController {
	
	@Autowired
	private ActorService actorService;	
	
	@GetMapping("/{actorNo}/{memberNo}") // 경로 변수 |@{/actor/{actorNo}(actorNo=${actor.actorNo})}| 로 보낼때
	public String goActorInfo(@PathVariable int actorNo, @PathVariable int memberNo, Model model) {
		//서비스에서 actor와 관련된 정보를 모두 조회해 model에 담음
		//나중엔 로그인중일때 memberNo도 넘어와야함
		actorService.getActorInfo(actorNo, memberNo, model);
		
		return "actorInfo_showMain/actorInfo";
	}
	
	@GetMapping("/actorEx")
	public String goActorExInfo() {
		return "actorInfo_showMain/actorInfo_ex";
		
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
