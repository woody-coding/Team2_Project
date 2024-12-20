package com.team2.project.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="MULTI24_MEMBER")
@Getter
@Setter
public class Member {
	
	@Id
	@Column(name="MEMBER_NO", nullable=false)
	private int memberNo;
	
	@Column(name="MEMBER_ID", nullable=false)
	private String memberId;
	
	@Column(name="MEMBER_PW", nullable=false)
	private String memberPw;
	
	@Column(name="MEMBER_NAME", nullable=false)
	private String memberName;
	
	@Column(name="MEMBER_PHONE", nullable=false)
	private String memberPhone;
	
	@Column(name="MEMBER_ADDR", nullable=false)
	private String memberAddr;
	
	@Column(name="MEMBER_STATUS", nullable=false)
	private String memberStatus;
	
	@Column(name="MEMBER_DATE", nullable=false)
	@Temporal(TemporalType.DATE)
	private Date memberDate;
}
