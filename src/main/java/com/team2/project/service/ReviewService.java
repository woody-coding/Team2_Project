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

    public List<Review> getReviewsByShowNo(int showNo) {
        return reviewRepository.findByShowNo(showNo);
    }

    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    public double getAverageScoreByShowNo(int showNo) {
        return reviewRepository.findAverageScoreByShowNo(showNo);
    }
    
    
    
}