package com.team2.project.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team2.project.model.Review;

public interface MypageReviewRepository extends JpaRepository<Review, Integer>{
	
	public List<Review> findAll(Sort sort);
	
	public Review findByReviewNo(int reviewNo);
}
