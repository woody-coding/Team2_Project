package com.team2.project.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.team2.project.model.Actor;
import com.team2.project.model.LikeYO;
import com.team2.project.model.Member;
import com.team2.project.model.Review;
import com.team2.project.repository.AdminActorRepository;
import com.team2.project.repository.AdminShowRepository;
import com.team2.project.repository.LikeYoRepository;
import com.team2.project.repository.MemberRepository;
import com.team2.project.repository.MypageReviewRepository;

@Service
public class MypageService {
	
	@Autowired
	private LikeYoRepository likeYoRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private MypageReviewRepository mypageReviewRepository;
	
	 public List<LikeYO> getLikedItems(int memberNo) {

	        List<LikeYO> allLikedItems = likeYoRepository.findLikedShowsAndActorsByMemberNo(memberNo);
	        
	        // 최대 6개
	        return allLikedItems.subList(0, Math.min(6, allLikedItems.size()));
	    }

    // 좋아요한 배우의 수를 반환하는 메서드
    public int getLikedActorCount(int memberNo) {
        return likeYoRepository.countByMemberNoAndActorNotNull(memberNo);
    }
	
	public Member getMember(int memberNo) {
	    return memberRepository.findById(memberNo)
	            .orElseThrow(() -> new NoSuchElementException("Member not found")); // 멤버가 없을 경우 예외 발생
	}
	
	//리뷰 리스트 내림차순으로 가져오기 (show 객체를 함께 로드)
	public List<Review> findAllReviewsWithShow(Sort sort) {
	    return mypageReviewRepository.findAllReviewsWithShow(sort);
	}
	
	//리뷰 상세보기
	public Review getReviewByReviewNo(int reviewNo) {
        return mypageReviewRepository.findByReviewNo(reviewNo);
	}
	
	//리뷰 업데이트
	public void updateReview(int reviewNo, String reviewTitle, String reviewContent, int reviewScore) {
        Review review = mypageReviewRepository.findById(reviewNo)
                .orElseThrow(() -> new NoSuchElementException("Review not found"));
        
        // 리뷰 정보 수정
        review.setReviewTitle(reviewTitle);
        review.setReviewContent(reviewContent);
        review.setReviewScore(reviewScore);
        
        // 업데이트된 리뷰 저장
        mypageReviewRepository.save(review);
    }
	
	public boolean validatePassword(Integer memberNo, String inputPassword) {
        // 사용자 정보를 조회
        Member member = memberRepository.findById(memberNo).orElse(null);
        
        if (member == null) {
            return false; // 사용자가 존재하지 않는 경우
        }

        // 저장된 비밀번호와 입력된 비밀번호 비교
        return inputPassword.equals(member.getMemberPw());
    }
	
}
