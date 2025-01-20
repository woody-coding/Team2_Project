package com.team2.project.DTO;

import java.sql.Date;

import lombok.Data;

@Data
public class MypagePaymentDTO {
	    private Long paymentId;
	    private String seatSpaces; // 좌석 정보
	    private String title; // 공연 제목
	    private String amount; // 가격
	    private String paymentStatus; // 결제 상태
	    private String fileNo;
	    private String orderId;
	    private Date showDate;
	    private String showTime;
	    
}
