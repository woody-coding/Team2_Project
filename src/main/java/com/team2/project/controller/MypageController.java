package com.team2.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.team2.project.DTO.MypageMemberDTO;
import com.team2.project.model.LikeYO;
import com.team2.project.model.Member;
import com.team2.project.model.Review;
import com.team2.project.service.MypageService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage")
public class MypageController {
	
	@Value("${file.upload-dir}")
	private String uploadDir;

	@Autowired
    private MypageService mypageService;

	@GetMapping("/main")
	public String mainPage(@SessionAttribute("login") Optional<Member> optionalMember, Model model, HttpSession session) {
	    // 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        Integer memberNo = optionalMember.get().getMemberNo();
	        
	        // 좋아요 정보 조회
	        List<LikeYO> likedItems = mypageService.getLikedItems(memberNo);
	        model.addAttribute("likedItems", likedItems);

	        // 좋아요 총 갯수 조회
	        int likedActorCount = mypageService.getLikedActorCount(memberNo);
	        model.addAttribute("likedActorCount", likedActorCount);

	        // 멤버 정보 조회
	        Member member = mypageService.getMember(memberNo);
	        model.addAttribute("member", member);
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }

	    return "mypage/mypageMain";
	}

	
	@GetMapping("/likeActor")
	public String LikeActor(@SessionAttribute("login") Optional<Member> optionalMember, Model model, HttpSession session) {
	    
		// 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        Integer memberNo = optionalMember.get().getMemberNo();
	    
	 // 좋아요 정보 조회
	    List<LikeYO> likedItems = mypageService.getLikedItems(memberNo);
        model.addAttribute("likedItems", likedItems);
        
     // 좋아요 총 갯수 조회
	    int likedActorCount = mypageService.getLikedActorCount(memberNo);
	    
	    model.addAttribute("likedActorCount", likedActorCount);
        
        // 멤버 정보 조회
	    Member member = mypageService.getMember(memberNo);
	    model.addAttribute("member", member);
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }
        
        return "mypage/mypageLikeActor";
	}
	
	@GetMapping("/reviewList")
	public String mypageReviewList(@SessionAttribute("login") Optional<Member> optionalMember, Model model, HttpSession session) {
	    // 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        Integer memberNo = optionalMember.get().getMemberNo();
	        
	        // 좋아요 정보 조회
	        List<LikeYO> likedItems = mypageService.getLikedItems(memberNo);
	        
	        // 좋아요 총 갯수 조회
	        int likedActorCount = mypageService.getLikedActorCount(memberNo);
	        
	        // 멤버 정보 조회
	        Member member = mypageService.getMember(memberNo);
	        
	        // 리뷰 목록 조회
	        List<Review> allReviews = mypageService.findAllReviewsWithShow(Sort.by(Sort.Direction.DESC, "reviewNo"));
	        
	        model.addAttribute("likedItems", likedItems);
	        model.addAttribute("likedActorCount", likedActorCount);
	        model.addAttribute("member", member);
	        model.addAttribute("allReviews", allReviews);
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }

	    return "mypage/mypageReviewList";
	}

	
	@GetMapping("/reviewDetail")
	public String mypageReviewDetail(@SessionAttribute("login") Optional<Member> optionalMember, @RequestParam int reviewNo, Model model, HttpSession session) {
		// 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        Integer memberNo = optionalMember.get().getMemberNo();
				 
				// 좋아요 정보 조회
				    List<LikeYO> likedItems = mypageService.getLikedItems(memberNo);
			        
			        
			     // 좋아요 총 갯수 조회
				    int likedActorCount = mypageService.getLikedActorCount(memberNo);
			        
			        // 멤버 정보 조회
				    Member member = mypageService.getMember(memberNo);
				    
				    Review review = mypageService.getReviewByReviewNo(reviewNo);
					
				    
				    model.addAttribute("likedItems", likedItems);
				    model.addAttribute("likedActorCount", likedActorCount);
				    model.addAttribute("member", member);
				    model.addAttribute("review", review);
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }
				    return "mypage/mypageReviewDetail";
	}
	
	@GetMapping("/reviewUpdateForm")
	public String mypageReviewUpdateForm(@SessionAttribute("login") Optional<Member> optionalMember, @RequestParam int reviewNo, Model model, HttpSession session) {
		// 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        Integer memberNo = optionalMember.get().getMemberNo();
		 
		// 좋아요 정보 조회
		    List<LikeYO> likedItems = mypageService.getLikedItems(memberNo);
	        
	        
	     // 좋아요 총 갯수 조회
		    int likedActorCount = mypageService.getLikedActorCount(memberNo);
		    
		 // 리뷰 정보 조회
	        Review review = mypageService.getReviewByReviewNo(reviewNo);
	        
	        // 멤버 정보 조회
		    Member member = mypageService.getMember(memberNo);
		    model.addAttribute("likedItems", likedItems);
		    model.addAttribute("likedActorCount", likedActorCount);
		    model.addAttribute("member", member);
		    model.addAttribute("review", review);
	} else {
        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
        return "redirect:/login";
    }
		return "mypage/mypageReviewUpdate";
	}
	
	@PostMapping("/reviewUpdate")
	public String mypageReviewUpdate(@SessionAttribute("login") Optional<Member> optionalMember, @RequestParam int reviewNo,
	        @RequestParam String reviewTitle,
	        @RequestParam String reviewContent,
	        @RequestParam(required = false) Integer reviewScore, Model model, HttpSession session) {
		
		// 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        Integer memberNo = optionalMember.get().getMemberNo();
		 
		 Review review = mypageService.getReviewByReviewNo(reviewNo);
		    if (review == null) {
		        // 리뷰가 존재하지 않는 경우 처리
		        return "redirect:/mypage/reviewList";
		    }

		    // 리뷰 정보 업데이트
		    review.setReviewTitle(reviewTitle);
		    review.setReviewContent(reviewContent);
		    if (reviewScore != null) {
		        review.setReviewScore(reviewScore);
		    }

		    // 업데이트된 리뷰 저장
		    mypageService.updateReview(reviewNo, review.getReviewTitle(), review.getReviewContent(), review.getReviewScore());
		 
		 
		// 좋아요 정보 조회
		    List<LikeYO> likedItems = mypageService.getLikedItems(memberNo);
	        
	        
	     // 좋아요 총 갯수 조회
		    int likedActorCount = mypageService.getLikedActorCount(memberNo);
	        
	        // 멤버 정보 조회
		    Member member = mypageService.getMember(memberNo);
		    
		    model.addAttribute("likedItems", likedItems);
		    model.addAttribute("likedActorCount", likedActorCount);
		    model.addAttribute("member", member);
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }
		return "redirect:/mypage/reviewDetail?reviewNo=" + reviewNo;
	}
	
	/*
	 * @GetMapping("/passwordCheck") public String showPasswordCheckPage(HttpSession
	 * session, Model model) {
	 * 
	 * // Integer memberNo = (Integer) session.getAttribute("memberNo");
	 * 
	 * Integer memberNo = 1; // 좋아요 정보 조회 List<LikeYO> likedItems =
	 * mypageService.getLikedItems(memberNo);
	 * 
	 * 
	 * // 좋아요 총 갯수 조회 int likedActorCount =
	 * mypageService.getLikedActorCount(memberNo);
	 * 
	 * // 멤버 정보 조회 Member member = mypageService.getMember(memberNo);
	 * 
	 * MypageMemberDTO mypageMemberDTO = new MypageMemberDTO();
	 * 
	 * model.addAttribute("likedItems", likedItems);
	 * model.addAttribute("likedActorCount", likedActorCount);
	 * model.addAttribute("member", member); model.addAttribute("mypageMemberDTO",
	 * mypageMemberDTO);
	 * 
	 * 
	 * return "mypage/mypageUserInfoPW"; // 비밀번호 확인 HTML 페이지의 이름 }
	 */
	
	/*
	 * @PostMapping("/memberCheck") public ResponseEntity<Map<String, Object>>
	 * mypageMemberCheck(
	 * 
	 * @RequestParam Integer memberNo,
	 * 
	 * @RequestParam String memberPw, HttpSession session) {
	 * 
	 * // 비밀번호가 null 또는 빈 문자열인지 확인 if (memberPw == null || memberPw.isEmpty()) {
	 * return ResponseEntity.badRequest().body(Map.of("success", false, "message",
	 * "비밀번호가 필요합니다.")); }
	 * 
	 * // 비밀번호 확인 로직 (서비스 메서드 호출) boolean isPasswordValid =
	 * mypageService.validatePassword(memberNo, memberPw);
	 * 
	 * Map<String, Object> response = new HashMap<>(); if (isPasswordValid) { //
	 * 비밀번호가 일치할 경우 추가 데이터를 가져와서 응답에 포함 List<LikeYO> likedItems =
	 * mypageService.getLikedItems(memberNo); int likedActorCount =
	 * mypageService.getLikedActorCount(memberNo); Member member =
	 * mypageService.getMember(memberNo);
	 * 
	 * response.put("success", true); response.put("likedItems", likedItems);
	 * response.put("likedActorCount", likedActorCount); response.put("member",
	 * member);
	 * 
	 * // 클라이언트에게 리다이렉트할 URL을 포함 response.put("redirectUrl", "/mypage/memberInfo");
	 * } else { response.put("success", false); response.put("message",
	 * "비밀번호가 일치하지 않습니다."); }
	 * 
	 * return ResponseEntity.ok(response); }
	 */
	
	//회원 정보
	@GetMapping("/memberInfo")
	public String mypageMemberInfo(@SessionAttribute("login") Optional<Member> optionalMember, Model model, HttpSession session) {
		// 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        Integer memberNo = optionalMember.get().getMemberNo();
				 
		   // 좋아요 정보 조회
		   List<LikeYO> likedItems = mypageService.getLikedItems(memberNo);
			        
		   
		   // 좋아요 총 갯수 조회
		   int likedActorCount = mypageService.getLikedActorCount(memberNo);
				    
			        
		   // 멤버 정보 조회
		   Member member = mypageService.getMember(memberNo);
		   
		   model.addAttribute("likedItems", likedItems);
		   model.addAttribute("likedActorCount", likedActorCount);
		   model.addAttribute("member", member);
	} else {
        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
        return "redirect:/login";
    }
		   return "mypage/mypageUserInfoDetail";
	}
}
