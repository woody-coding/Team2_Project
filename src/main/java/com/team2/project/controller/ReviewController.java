package com.team2.project.controller;

import com.team2.project.DTO.ShowDetailDTO;
import com.team2.project.model.Payment;
import com.team2.project.model.Review;
import com.team2.project.model.TestMember; // TestMember로 변경
import com.team2.project.service.ShowDetailService;
import com.team2.project.service.PaymentServiceforReview;
import com.team2.project.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/review")
@SessionAttributes("loginUser") // 세션에 로그인 사용자 저장
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ShowDetailService showService;
    
    @Autowired
    private PaymentServiceforReview paymentService;

    // 테스트 로그인 사용자 설정 : 취합시 수정필요
    @GetMapping("/testLogin")
    public String testLogin(Model model) {
        TestMember testMember = new TestMember();
        testMember.setMemberNo(2); // 테스트용 회원 번호 설정
        testMember.setMemberName("Test User");
        model.addAttribute("loginUser", testMember); // 세션에 저장
        return "redirect:/review/reviewWrite?showNo=1";
        
        //참고 사항
        // 1.테스트 로그인 실행 /review/testLogin으로 접속하면, TestMember 객체가 세션에 저장됩니다.
        // 2. 리뷰 작성 페이지 접근 / /review/reviewWrite?showNo=1으로 접속하면 세션에 저장된 TestMember 정보를 기반으로 접근을 허용
        // 3. 리뷰 작성 및 제출 / 제출 시 세션의 memberNo 정보가 리뷰 데이터에 포함되어 저장됩니다.
    }

    // 리뷰 작성 페이지 접근
    @GetMapping("/reviewWrite")
    public String reviewWrite(
            @RequestParam("showNo") int showNo,
            @SessionAttribute(value = "loginUser", required = false) TestMember loginUser, // TestMember 사용
            Model model) {

        // 로그인 여부 확인
        if (loginUser == null) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

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
            @SessionAttribute("loginUser") TestMember loginUser,
            Model model) { // TestMember 사용
    	
    	
    	// 리뷰 내용 길이 검사
        if (reviewContent.length() < 110) {
            // 110자 미만인 경우 에러 메시지 추가
            model.addAttribute("errorMessage", "리뷰 내용은 최소 110자 이상이어야 합니다.");
            return "ticket/reviewPage/reviewpage"; // 리뷰 작성 페이지로 다시 돌아가기
        }
    	
    	
        // 세션의 로그인 정보에서 회원 번호 가져오기
        int memberNo = loginUser.getMemberNo();

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
