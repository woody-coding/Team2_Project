package com.team2.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team2.project.model.Seat;
import com.team2.project.model.id.SeatId;

public interface SeatRepository extends JpaRepository<Seat, SeatId> {
    
    boolean existsByShowNoAndSeatNoAndShowDate(int showNo, int seatNo, java.sql.Date showDate);
    
}
