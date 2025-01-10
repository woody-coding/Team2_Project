package com.team2.project.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team2.project.model.Seat;


public interface SeatRepo extends JpaRepository<Seat, Integer> {

	List<Seat> findByShowNoAndShowDate(int showNo, Date showDate);

}
