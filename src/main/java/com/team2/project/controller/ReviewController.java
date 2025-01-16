package com.team2.project.controller;

import com.team2.project.DTO.ShowDetailDTO;
import com.team2.project.model.Member;
import com.team2.project.model.Review;
import com.team2.project.service.ShowDetailService;
import com.team2.project.service.ReviewService;
import com.team2.project.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/review")
@SessionAttributes("login")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ShowDetailService showService;
    
    @Autowired
    private PaymentService paymentService;

    // 리뷰 작성 페이지 접근
    //로그인 시 접근가능하도록 처리 (SessionAttribute("login")추가 Optional<Member> optionalMember 처리
    @GetMapping("/reviewWrite")
    public String reviewWrite(
            @RequestParam("showNo") int showNo,
            @SessionAttribute("login") Optional<Member> optionalMember,
            Model model) {


        // 공연 정보 조회
        Optional<ShowDetailDTO> optionalShow = showService.getShowDetail(showNo);

        if (optionalShow.isPresent()) {
            ShowDetailDTO show = optionalShow.get();
            
            // 결제 정보 확인
            Optional<Payment> paymentOpt = paymentService.findByMemberNoAndOrderNameAndPaySuccessYN(
                    loginUser.getMemberNo(), show.getShowTitle(), true);

            if (paymentOpt.isEmpty()) {
                model.addAttribute("showNo", showNo);
                
                return "ticket/reviewPage/reviewError"; // 에러 페이지로 이동
            }

            model.addAttribute("showNo", show.getShowNo());
            model.addAttribute("showTitle", show.getShowTitle());
        } else {
            throw new IllegalArgumentException("유효하지 않은 공연 번호입니다.");
        }

        return "ticket/reviewPage/reviewpage";
    }

    // 리뷰 제출
    @PostMapping("/reviewWrite")
    public String submitReview(
            @RequestParam("showNo") int showNo,
            @RequestParam("reviewTitle") String reviewTitle,
            @RequestParam("reviewContent") String reviewContent,
            @RequestParam("reviewScore") int reviewScore,
            @SessionAttribute("login") Optional<Member> optionalMember,
            Model model) { // "login"시 처리할수있도록 Optional<Member> optionalMember 처리
    	
    	
    	// 리뷰 내용 길이 검사
        if (reviewContent.length() < 110) {
            // 110자 미만인 경우 에러 메시지 추가
            model.addAttribute("errorMessage", "리뷰 내용은 최소 110자 이상이어야 합니다.");
            return "ticket/reviewPage/reviewpage"; // 리뷰 작성 페이지로 다시 돌아가기
        }
    	
    	
     // 세션의 로그인 정보에서 회원 번호 가져오기
        Member member = optionalMember.orElseThrow(() -> new IllegalStateException("로그인이 필요합니다."));
        int memberNo = member.getMemberNo();

        Review review = new Review();
        review.setShowNo(showNo);
        review.setMemberNo(memberNo);
        review.setReviewTitle(reviewTitle);
        review.setReviewContent(reviewContent);
        review.setReviewScore(reviewScore);
        review.setReviewDate(LocalDate.now());

        reviewService.saveReview(review);

        return "redirect:/show/ticketDetailPage/" + showNo;
    }
    
    
}