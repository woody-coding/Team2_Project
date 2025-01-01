package com.team2.project.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team2.project.model.Show;

@Repository
public interface AdminShowRepository extends JpaRepository<Show, Integer>{

public List<Show> findAll();
	
	public List<Show> findAll(Sort sort);
	
	public Show save(Show show);
	
	public Show findByShowNo(int showNo);
}
