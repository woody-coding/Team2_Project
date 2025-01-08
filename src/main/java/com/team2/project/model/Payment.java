package com.team2.project.model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "MULTI24_PAYMENT")
public class Payment {
	
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "payment_id", nullable = false, unique = true)
    private Long paymentId;
    
    @Column(name = "payment_type")
    private String payType;
    
    @Column(nullable = false , name = "payment_amount")
    private Long amount;
    
    @Column(name = "payment_order_name")
    private String orderName;
    
    @Column(nullable = false , name = "payment_order_id")
    private String orderId;

    @Column(name = "payment_paysuccess_yn")
    private boolean paySuccessYN;
    
    @ManyToOne
    @JoinColumn(name = "memberNo")
    private Member member;
    
    @Column(name = "payment_key")
    private String paymentKey;
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    
    @Column(name = "show_date")
    private Date showDate;

    @Column(name = "show_time")
    private String showTime;

    @Column(name = "seat_spaces")
    private String seatSpaces;
    
    @Column(name = "member_name")
    private String memberName;
    
    // 좌석과의 관계 설정 (1:N)
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<Seat> seats;
 
}