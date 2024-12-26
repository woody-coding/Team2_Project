package com.team2.project.DTO;

import java.sql.Date;
import com.team2.project.model.Member;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;


public class AdminDTO {
	
	@Data
	public class AdminMList {
		private int memberNo;
		private String memberId;
		private String memberPw;
		private String memberName;
		private String memberPhone;
		private String memberAddr;
		private String memberStatus;
		@Temporal(TemporalType.DATE)
		private Date memberDate;
	}
}
