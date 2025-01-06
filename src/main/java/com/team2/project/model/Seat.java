package com.team2.project.model;

import java.util.Date;

import com.team2.project.model.id.SeatId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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

@IdClass(SeatId.class) // SeatId 클래스를 ID 클래스로 설정
@Entity
@Table(name = "MULTI24_SEAT")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @Column(name = "SEAT_NO", nullable = false)
    private int seatNo;

    @Id
    @Column(name = "SHOW_NO", nullable = false)
    private int showNo;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "SHOW_DATE", nullable = false)
    private Date showDate;

    @Column(name = "SEAT_SPACE", nullable = false)
    private String seatSpace;

    @Column(name = "IS_BOOK", nullable = false)
    private String isBook;
    
    @Column(name = "TOT_SEAT", nullable = false)
    private int totSeat;
    
    @Column(name = "LEFT_SEAT", nullable = false)
    private int leftSeat;

    @Column(name = "SHOW_TIME", nullable = false)
    private String showTime;

    @ManyToOne
    @JoinColumn(name = "SHOW_NO", referencedColumnName = "SHOW_NO", insertable = false, updatable = false)
    private Show show;
    
    @ManyToOne // 하나의 결제에 여러 개의 좌석이 연결될 수 있음
    @JoinColumn(name = "PAYMENT_ID", nullable = true) // 외래 키 설정 (결제 ID), null 허용
    private Payment payment;

