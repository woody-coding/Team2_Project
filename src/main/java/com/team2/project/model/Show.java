package com.team2.project.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "show_seq")
    @SequenceGenerator(name = "show_seq", sequenceName = "MULTI24_SHOW_SEQ", allocationSize = 1)
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
	
	@Column(name = "SHOW_CATE", length = 30, nullable = false)
	private String showCate;
	
	@Column(name = "SHOW_START_TIME" , length = 20,  nullable = false)
	private String showStartTime;
	
	@ManyToOne
	@JoinColumn(name = "FILE_NO", insertable = false, updatable = false)
	private ShowActorFile showActorFile;
	
	@Column(name = "SHOW_PLAYTIME", nullable = false)
	private Integer showPlayTime;
	
	@Column(name = "SHOW_RATING", length = 20, nullable = false)
	private String showRating;
	
	@Column(name = "FILE_NO") 
	private String fileNo;

	//추가 - 수빈님요청으로 추가된 컬럼
	@Column(name = "OPEN_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date openDate;

	//추가 - 공연찜하기 기능 제외로 인위적으로 추가한 공연 좋아요 수 컬럼
	@Column(name = "Likes", nullable = false)
	private int likes;

	//Show Entity에 있던 total,left seat 컬럼 Seat Entity로 이동

}
