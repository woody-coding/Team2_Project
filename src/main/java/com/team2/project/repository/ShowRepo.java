package com.team2.project.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team2.project.model.Show;

@Repository
public interface ShowRepo  extends JpaRepository<Show, Integer> {

	List<Show> findByOpenDateBeforeAndEndDateAfter(Date openDate, Date endDate);
	
	List<Show> findTop7ByOrderByLikesDesc();
	 
	List<Show> findByOpenDateGreaterThanEqualOrderByOpenDateAsc(Date today);

	Show findByShowNo(int showNo);
	
}
