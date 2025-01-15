package com.team2.project.repository;

import com.team2.project.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // 특정 공연 번호의 리뷰를 내림차순으로 정렬하여 가져오기
	 @Query("SELECT r FROM Review r WHERE r.showNo = :showNo ORDER BY r.reviewNo DESC")
	    List<Review> findAllByShowNoOrderByReviewNoDesc(@Param("showNo") int showNo);

    // 특정 공연 번호의 평균 평점 계산
    @Query("SELECT AVG(r.reviewScore) FROM Review r WHERE r.showNo = :showNo")
    double getAverageScoreByShowNo(@Param("showNo") int showNo);
}
