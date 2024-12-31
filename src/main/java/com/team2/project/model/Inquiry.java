package com.team2.project.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "MULTI24_INQUIRY")
public class Inquiry {

	@Id
	@Column(name = "INQUIRY_NO")
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int inquiryNo;
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_NO", updatable = false)
	private Member member;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "INQUIRY_CATE", nullable = false)
	private InquiryCategory inquiryCategory;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "INQUIRY_DATE", nullable = false)
	private LocalDate inquiryDate;
	
	@Column(name = "INQUIRY_TITLE", nullable = false, length = 35)
	private String inquiryTitle;
	
	@Column(name = "INQUIRY_CONTENT", nullable = false, length = 300)
	private String inquiryContent;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "INQUIRY_STATUS", nullable = false)
	private InquiryStatus inquiryStatus;
	
	@Column(name = "INQUIRY_ANSWER", length = 500)
	private String inquiryAnswer;
	
	@Column(name = "INQUIRY_ANSWERDATE")
	private LocalDate inquiryAnswerDate;
			
	// 이미지 파일 리스트 추가
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<InquiryFile> inquiryFiles;
	
	public Inquiry() {
		 this.inquiryStatus = InquiryStatus.ANSWER_PROCESSING;
		 this.inquiryFiles = new ArrayList<>();
	}

	public Inquiry(int inquiryNo, Member member, InquiryCategory inquiryCategory, LocalDate inquiryDate,
			String inquiryTitle, String inquiryContent, InquiryStatus inquiryStatus, String inquiryAnswer,
			LocalDate inquiryAnswerDate) {
		this.inquiryNo = inquiryNo;
		this.member = member;
		this.inquiryCategory = inquiryCategory;
		this.inquiryDate = inquiryDate;
		this.inquiryTitle = inquiryTitle;
		this.inquiryContent = inquiryContent;
		this.inquiryStatus = InquiryStatus.ANSWER_PROCESSING;
		this.inquiryAnswer = inquiryAnswer;
		this.inquiryAnswerDate = inquiryAnswerDate;
		this.inquiryFiles = new ArrayList<>();
	}
}