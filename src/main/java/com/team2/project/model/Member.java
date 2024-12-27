package com.team2.project.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="MULTI24_MEMBER")
@Getter
@Setter
@ToString
public class Member {
	
	@Id
	@Column(name="MEMBER_NO", nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
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
	
	@Column(name="MEMBER_ADDRDE", nullable=false)
	private String memberAddrde;
	
	@Column(name="MEMBER_STATUS", nullable=false)
	private Boolean memberStatus;
	
	@Column(name="MEMBER_DATE", nullable=false)
	@Temporal(TemporalType.DATE)
	private Date memberDate;

	public Member() {}

	public Member(int memberNo, String memberId, String memberPw, String memberName, String memberPhone,
			String memberAddr, String memberAddrde, Boolean memberStatus, Date memberDate) {
		super();
		this.memberNo = memberNo;
		this.memberId = memberId;
		this.memberPw = memberPw;
		this.memberName = memberName;
		this.memberPhone = memberPhone;
		this.memberAddr = memberAddr;
		this.memberAddrde = memberAddrde;
		this.memberStatus = memberStatus;
		this.memberDate = memberDate;
	}

	
	
}
