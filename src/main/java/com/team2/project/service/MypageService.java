package com.team2.project.service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.team2.project.DTO.MypagePaymentDTO;
import com.team2.project.model.Actor;
import com.team2.project.model.LikeYO;
import com.team2.project.model.Member;
import com.team2.project.model.Payment;
import com.team2.project.model.Review;
import com.team2.project.model.Seat;
import com.team2.project.model.Show;
import com.team2.project.repository.AdminActorRepository;
import com.team2.project.repository.AdminShowRepository;
import com.team2.project.repository.LikeYoRepository;
import com.team2.project.repository.MemberRepository;
import com.team2.project.repository.MypageReviewRepository;
import com.team2.project.repository.PaymentRepository;

@Service
public class MypageService {
	
	@Autowired
	private LikeYoRepository likeYoRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private MypageReviewRepository mypageReviewRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
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
	
	// 리뷰 목록 조회 (memberNo 기준)
	public List<Review> findReviewsByMemberNo(int memberNo, Sort sort) {
	    return mypageReviewRepository.findReviewsByMemberNo(memberNo, sort);
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
	
	public List<MypagePaymentDTO> getPaymentsByMemberNo(int memberNo) {
	    List<Payment> payments = paymentRepository.findByMember_MemberNoAndPaySuccessYNTrue(memberNo);
	    
	    // 주문번호를 기준으로 그룹화
	    Map<String, MypagePaymentDTO> groupedPayments = new HashMap<>();
	    
	    for (Payment payment : payments) {
	        MypagePaymentDTO dto;
	        String currentOrderId = payment.getOrderId(); // 현재 주문 ID
	        
	        if (groupedPayments.containsKey(currentOrderId)) {
	            // 이미 존재하는 결제 항목이면 좌석 정보를 추가
	            dto = groupedPayments.get(currentOrderId);
	            dto.setSeatSpaces(dto.getSeatSpaces() + ", " + payment.getSeatSpaces());
	        } else {
	            // 새로운 결제 항목 추가
	            dto = new MypagePaymentDTO();
	            dto.setPaymentId(payment.getPaymentId());
	            dto.setSeatSpaces(payment.getSeatSpaces()); // 처음 좌석 정보
	            dto.setShowDate(payment.getShowDate());
	            dto.setShowTime(payment.getShowTime());
	            dto.setOrderId(currentOrderId); // 주문 ID 설정
	            
	            // 좌석 정보를 통해 Show 정보 가져오기
	            List<Seat> seats = payment.getSeats(); // 결제와 연결된 좌석 목록 가져오기
	            
	            // 금액 포맷
	            NumberFormat formatter = NumberFormat.getInstance(Locale.KOREA);
	            String formattedAmount = formatter.format(payment.getAmount()); // 포맷된 금액

	            if (seats != null && !seats.isEmpty()) {
	                dto.setAmount(formattedAmount); // 포맷된 금액 사용
	                Seat firstSeat = seats.get(0); // 첫 번째 좌석을 기준으로 Show 정보 가져오기
	                Show show = firstSeat.getShow(); // Show 엔티티 가져오기
	                if (show != null) {
	                    dto.setTitle(show.getShowTitle());
	                    dto.setFileNo(show.getFileNo());
	                }
	            } else {
	                dto.setAmount(formattedAmount); // 좌석이 없을 경우 포맷된 전체 금액 설정
	            }
	            
	            groupedPayments.put(currentOrderId, dto); // 그룹에 추가
	        }
	    }
	    
	    return new ArrayList<>(groupedPayments.values()); // 그룹화된 결제 목록 반환
	}

}
