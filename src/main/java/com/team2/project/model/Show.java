package com.team2.project.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="MULTI24_SHOW")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Show {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "SHOW_NO")
	private int showNo;
	
	@Column(name = "SHOW_PRICE", length = 20, nullable = false)
	private String showPrice;
	
	@Column(name = "START_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(name = "END_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@Column(name = "SHOW_TITLE", length = 1000, nullable = false)
	private String showTitle;
	
	@Column(name = "SHOW_INFO", length = 4000, nullable = false)
	private String showInfo;
	
	@Column(name = "TOT_SEAT", nullable = false)
	private int totSeat;
	
	@Column(name = "LEFT_SEAT", nullable = false)
	private int leftSeat;
	
	@Column(name = "SHOW_CATE", length = 30, nullable = false)
	private String showCate;
	
	@Column(name = "SHOW_TIME", nullable = false)
	@Temporal(TemporalType.TIME)
	private Date showTime;
	
	@ManyToOne
	@JoinColumn(name = "FILE_NO", insertable = false, updatable = false)
	private ShowActorFile showActorFile;
}
