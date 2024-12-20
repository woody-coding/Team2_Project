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
public class MEMBER {
	
	@Id
	@Column(name="MEMBER_NO", nullable=false)
	private int member_no;
	
	@Column(name="MEMBER_ID", nullable=false)
	private String member_id;
	
	@Column(name="MEMBER_PW", nullable=false)
	private String member_pw;
	
	@Column(name="MEMBER_NAME", nullable=false)
	private String member_name;
	
	@Column(name="MEMBER_PHONE", nullable=false)
	private String member_phone;
	
	@Column(name="MEMBER_ADDR", nullable=false)
	private String member_addr;
	
	@Column(name="MEMBER_STATUS", nullable=false)
	private String member_status;
	
	@Column(name="MEMBER_DATE", nullable=false)
	@Temporal(TemporalType.DATE)
	private Date member_date;
}
