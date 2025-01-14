package com.team2.project.service;

import com.team2.project.model.Review;
import com.team2.project.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // 특정 ShowNo에 대한 리뷰를 내림차순으로 가져오기
    public List<Review> getReviewsByShowNo(int showNo) {
        return reviewRepository.findAllByShowNoOrderByReviewNoDesc(showNo);
    }

    // 리뷰 저장
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    // 평균 평점 계산
    public double getAverageScoreByShowNo(int showNo) {
        return reviewRepository.getAverageScoreByShowNo(showNo);
    }
}
