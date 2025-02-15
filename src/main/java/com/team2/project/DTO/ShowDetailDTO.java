package com.team2.project.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import com.team2.project.model.ShowActorFile;

@Getter
@Setter
public class ShowDetailDTO {
    private int showNo;
    private String showPrice;
    private Date startDate;
    private Date endDate;
    private String showTitle;
    private String showInfo;
    private int totSeat;
    private int leftSeat;
    private String showCate;
    private int showPlayTime; //러닝타임
    private String showRating; //시청제한연령
    private String showStartTime; // 1회차, 2회차 등 회차시간
    private Date openDate;
    private String fileNo;
    
    private ShowActorFile showActorFile;
    
    private double averageScore; // 평균 평점 필드 추가
}
