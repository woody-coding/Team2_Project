package com.team2.project.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "MULTI24_SEAT")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
	@Id
	@Column(name = "SEAT_NO")
	private int seatNo;
	
	@Column(name = "SHOW_NO")
	private int showNo; //fk
	
	@Column(name = "SEAT_SPACE", nullable = false)
	private String seatSpace;
	
	@Column(name = "IS_BOOK", nullable = false)
	private String isBook;
	
	@Column(name = "SHOW_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
	private Date showDate;
	
	@Column(name = "SHOW_TIME", nullable = false)
    @Temporal(TemporalType.TIME)
	private Date showTime;
	
	@ManyToOne
	@JoinColumn(name = "SHOW_NO" , insertable = false, updatable = false)
	private Show show;

}
