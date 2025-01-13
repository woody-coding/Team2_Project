package com.team2.project.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team2.project.model.Review;

public interface MypageReviewRepository extends JpaRepository<Review, Integer>{
	
	public List<Review> findAll(Sort sort);
	
	@Query("SELECT r FROM Review r JOIN FETCH r.show WHERE r.reviewNo = :reviewNo")
	Review findByReviewNo(@Param("reviewNo") int reviewNo);

	    @Query("SELECT r FROM Review r JOIN FETCH r.show WHERE r.member.memberNo = :memberNo")
	    List<Review> findReviewsByMemberNo(@Param("memberNo") int memberNo, Sort sort);

}
