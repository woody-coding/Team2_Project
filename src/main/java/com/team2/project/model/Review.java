package com.team2.project.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "MULTI24_REVIEW")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Review {
	
    @Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	@Column(name="REVIEW_NO")
    private int ReviewNo;
    
    @Column(name = "SHOW_NO", nullable = false)
    private int showNo;  
    
    @Column(name = "MEMBER_NO", nullable = false)
    private int memberNo;

    @Column(name = "REVIEW_TITLE", length = 100 , nullable = false)
    private String reviewTitle;

    @Column(name = "REVIEW_CONTENT", length = 1000 , nullable = false)
    private String reviewContent;

    @Column(name = "REVIEW_DATE" , nullable = false)
    private LocalDate reviewDate;

    @Column(name = "REVIEW_EDIT", length = 2 , nullable = false)
    private String reviewEdit;

    @Column(name = "REVIEW_SCORE" , nullable = false)
    private Integer reviewScore;
	
    @ManyToOne
	@JoinColumn(name="SHOW_NO", insertable = false, updatable = false)
	private Show show;
	
	@ManyToOne
	@JoinColumn(name="MEMBER_NO", insertable = false, updatable = false)
	private Member member;
	
}
