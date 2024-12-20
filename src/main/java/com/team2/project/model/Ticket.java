package com.team2.project.model;

import java.time.LocalDate;
import java.time.LocalTime;

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
@Table(name = "MULTI24_TICKET")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	@Column(name="TICKET_NO")
    private int ticketNo;
	
    @Column(name = "MEMBER_NO", nullable = false)
    private int memberNo;
    
    @Column(name = "SHOW_NO", nullable = false)
    private int showNo;
	
	@Column(name = "TICKET_STATE", nullable = false)
    private String ticketState;

    @Column(name = "TICKET_PRICE", nullable = false)
    private String ticketPrice;

    @Column(name = "SHOW_TITLE", nullable = false, length = 1000)
    private String showTitle;

    @Column(name = "SEAT_SPACE", nullable = false, length = 10)
    private String seatSpace;

    @Column(name = "SHOW_DATE", nullable = false)
    private LocalDate showDate;

    @Column(name = "SHOW_TIME", nullable = false)
    private LocalTime showTime;
    
    @ManyToOne
	@JoinColumn(name="MEMBER_NO", insertable = false, updatable = false)
	private Member member;
    
    @ManyToOne
	@JoinColumn(name="SHOW_NO", insertable = false, updatable = false)
	private Show show;
	
} 
