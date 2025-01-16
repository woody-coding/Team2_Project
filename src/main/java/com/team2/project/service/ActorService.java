package com.team2.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.team2.project.repository.ActorRepo;
import com.team2.project.repository.LikeRepo;
import com.team2.project.repository.ShowActorRepo;
import com.team2.project.repository.ShowRepo;
import com.team2.project.repository.FileRepo;
import com.team2.project.DTO.ShowActorDto;
import com.team2.project.model.Actor;
import com.team2.project.model.LikeYO;
import com.team2.project.model.Show;
import com.team2.project.model.ShowActor;
import com.team2.project.model.ShowActorFile;

@Service
public class ActorService {
	
	@Autowired
	private ActorRepo actorRepo; // Actor 정보 처리
	
	@Autowired
	private ShowActorRepo showActorRepo;
	
	@Autowired
	private LikeRepo likeRepo;	
	
	@Autowired
	private FileRepo fileRepo;	
	
	@Autowired
	private ShowRepo showRepo;	
	
	
	public void getActorInfo(int actorNo, int memberNo, Model model) {
		Actor actor = actorRepo.findByActorNo(actorNo);
		
		ShowActorFile actorFile = fileRepo.findByActor(actor);
		String actorPhoto = null;
		if(actorFile != null){
			actorPhoto = actorFile.getFilePath();
		}
		
		// 다른작품 가져오기
		List<ShowActor> showActors = showActorRepo.findByActorNo(actorNo);
		List<ShowActorDto> dtos = new ArrayList<>();
		
		for(ShowActor showActor : showActors) {
			ShowActorDto tmp = new ShowActorDto();
			tmp.setShowNo(showActor.getShowNo());
			tmp.setActorNo(showActor.getActorNo());
			tmp.setRoleName(showActor.getRoleName());
			
			//배우 이름 가져오기 
			Actor Actor1 = actorRepo.findByActorNo(showActor.getActorNo());
			tmp.setActorName(Actor1.getActorName());
			
			// 공연 타이틀 가져오기
			Show show1 = showRepo.findByShowNo(showActor.getShowNo());
			tmp.setShowTitle(show1.getShowTitle());
			
			ShowActorFile fileTmp = fileRepo.findByShow(show1);
			if(fileTmp != null) {
				tmp.setFilePath(fileTmp.getFilePath());
			}else {
				tmp.setFilePath(null);
			}
			
			dtos.add(tmp);
			
		}

		// 수상정보 가져오기
		String actorInfo = actor.getActorInfo();
		String[] awards = actorInfo.split("/");	
		
		// 좋아요 상태 가져오기
		String status;
		if(memberNo == 0) {
			status = "N";
		}
		else {
			LikeYO likeTmp = likeRepo.findByMemberNoAndActorNo(memberNo, actorNo).orElse(null);
			status = likeTmp != null ? likeTmp.getStatus() : "N";
		}
		
		model.addAttribute("awards", awards);
		model.addAttribute("actor", actor);
		model.addAttribute("dtos", dtos);
		model.addAttribute("actorPhoto", actorPhoto);
		model.addAttribute("memberNo", memberNo);
		model.addAttribute("status", status);
	}
	
	public boolean toggleLike(LikeYO likeYo) {
		try {
			//기존 데이터 조회
			Optional<LikeYO> existingLike = likeRepo.findByMemberNoAndActorNo(likeYo.getMemberNo(), likeYo.getActorNo());
			
			if(existingLike.isPresent()) {
				// 값이 있는지 확인. 값이 있으면 상태 업데이트
				LikeYO tmp = existingLike.get();
				tmp.setStatus(likeYo.getStatus());
				likeRepo.save(tmp);
				likeRepo.flush();
			} else {
				// 없으면 새로운 데이터 추가
				LikeYO newLike = new LikeYO();
				newLike.setMemberNo(likeYo.getMemberNo());
				newLike.setActorNo(likeYo.getActorNo());
	            newLike.setShowNO(likeYo.getShowNO());
	            newLike.setStatus(likeYo.getStatus());
	            likeRepo.save(newLike);
	            likeRepo.flush();
			}
			
			return true;
			 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
