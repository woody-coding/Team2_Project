package com.team2.project.DTO;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminShowDTO {
	
	    private int showNo;            // 공연 번호
	    private String showPrice;      // 공연 가격
	    private Date startDate;        // 시작 날짜
	    private Date endDate;          // 종료 날짜
	    private String showTitle;      // 공연 제목
	    private String showInfo;       // 공연 정보
	    private int totSeat;           // 총 좌석 수
	    private int leftSeat;          // 남은 좌석 수
	    private String showCate;       // 공연 카테고리
	    private String showTime;         // 공연 시간
	    private String showPlace;      // 공연 장소
	    private String showPlayTime;  // 공연 재생 시간
	    private String showRating;      // 공연 평점
	    
	    public void setShowPlayTime(String showPlayTime) {
	        this.showPlayTime = showPlayTime;
	    }
}
