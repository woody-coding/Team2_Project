package com.team2.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team2.project.model.Show;

public interface ShowRepository extends JpaRepository<Show, Integer>{

	public Show findByShowNo(int showNo);
}
