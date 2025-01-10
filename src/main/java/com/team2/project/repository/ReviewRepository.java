package com.team2.project.repository;

import com.team2.project.model.Review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByShowNo(int showNo);
    
    // 평균 평점 계산
    @Query("SELECT COALESCE(AVG(r.reviewScore), 0) FROM Review r WHERE r.showNo = :showNo")
    Double findAverageScoreByShowNo(@Param("showNo") int showNo);
}