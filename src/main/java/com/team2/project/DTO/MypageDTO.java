package com.team2.project.DTO;

import java.util.Date;

import com.team2.project.model.Actor;
import com.team2.project.model.Show;
import com.team2.project.model.ShowActorFile;
import com.team2.project.model.LikeYO;
import com.team2.project.model.Member;

import lombok.Data;

public class MypageDTO {
	//마이페이지 메인DTO
	@Data
	public static class MypageMainDTO{
		private int memberNo;
		private String memberName; // 사용자 이름
	    private int likedShowCount; // 찜한 공연 수
	    private int likedActorCount; // 찜한 배우 수
	    private int showNo; // 찜한 공연 ID
	    private int actorNo; // 찜한 배우 ID
	    private Show likedShow; // 찜한 공연
	    private Actor likedActor; // 찜한 배우
	    private LikeYO likedItem; // 사용자가 찜한 항목
	    private String fileNo;
	    private String filePath;
	    private String fileName;
	    private Date fileDate;
	    
	}
	
}
