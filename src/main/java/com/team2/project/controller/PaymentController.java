package com.team2.project.controller;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team2.project.DTO.OrderDetailsDTO;
import com.team2.project.DTO.ShowTitleAndPriceDTO;
import com.team2.project.model.Member;
import com.team2.project.model.Payment;
import com.team2.project.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@SessionAttributes("login")
@RequiredArgsConstructor
@RequestMapping("/payment")
@Controller
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 요청 전, 결제 방식 선택하는 페이지로 이동
    @GetMapping("/checkout")
    public String showCheckoutPage(
    		@SessionAttribute("login") Member member,
    		@RequestParam(required = false) Date showDate,
    		@RequestParam(required = false) Integer showNo,
    		@RequestParam(required = false) List<String> selectedSeats,
    		HttpServletRequest request,
    		Model model, 
    		RedirectAttributes redirectAttributes) {
        
    	int seatNo1 = Integer.parseInt(selectedSeats.get(0));
    	int seatNo2;
        ShowTitleAndPriceDTO showDTO = paymentService.getShowTitleAndPrice(showNo);
        int amount = Integer.parseInt(showDTO.getShowPrice());
    	
    	switch(selectedSeats.size()) {
    	case 1:
    		seatNo2 = 0;
    		break;
    		
    	case 2:
    		seatNo2 = Integer.parseInt(selectedSeats.get(1));
    		amount = amount * 2;
    		break;
    	
    	default:
    		 redirectAttributes.addAttribute("message", "좌석 선택이 올바르지 않습니다.");
    		 return "redirect:/payment/fail";
    	}
    	
    	if (!paymentService.areSeatAvailable(showNo, seatNo1, seatNo2, showDate)) {
    	    // 좌석이 이미 결제되었거나 사용 불가능한 경우
    	    model.addAttribute("message", "이미 결제된 좌석입니다.");
    	    return "/payment/checkout";  // 같은 페이지로 다시 렌더링
    	}


        model.addAttribute("orderName", showDTO.getShowTitle());
        model.addAttribute("amount", amount);
        model.addAttribute("showDate", showDate);
        model.addAttribute("showNo", showNo);
        model.addAttribute("seatNo1", seatNo1);
        if (seatNo2 != 0) {
            model.addAttribute("seatNo2", seatNo2);
        }
        return "/payment/checkout";
    }
    
    
    
    // 결제 요청 전, 미리 orderId, amount 값을 임시 저장해두기
    @PostMapping("/save")
    public ResponseEntity<String> saveOrderDetails(
    		@SessionAttribute("login") Member member,
    		@RequestBody OrderDetailsDTO orderDetails) {
        try {
            paymentService.saveOrderDetails(orderDetails, member);
            return ResponseEntity.ok("주문 정보 저장 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 정보 저장 실패");
        }
    }
    


    // 결제 요청 성공 시, 결제 진행 중 페이지로 이동 (동시에 결제 승인 요청 진행)
    @GetMapping("/request/success")
    public String approveProcessingPage(
    		@SessionAttribute("login") Member member,
    		@RequestParam(required = false) Date showDate,
    		@RequestParam(required = false) Integer showNo,
    		@RequestParam(required = false) Integer seatNo1, 
            @RequestParam(required = false) Integer seatNo2,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            Model model) {
    	
    	model.addAttribute("showDate", showDate);
    	model.addAttribute("showNo", showNo);
    	model.addAttribute("seatNo1", seatNo1);
        if (seatNo2 != 0) {
            model.addAttribute("seatNo2", seatNo2);
        }
    	return "/payment/processing";
    }
    
    
    // 결제 요청 실패 시, 실패 페이지로 이동 (에러 메시지 전달하기)
    @GetMapping("/request/fail")
    public String toRequestFailPage(@SessionAttribute("login") Member member) {
    	return "redirect:/payment/fail";
    }
    
    
    
    // 결제 승인 요청 처리
    @PostMapping("/confirm")
    public ResponseEntity<JSONObject> confirmPayment(
    		@SessionAttribute("login") Member member,
    		@RequestParam(required = false) Date showDate,
    		@RequestParam(required = false) Integer showNo,
    		@RequestBody String jsonBody,
    		@RequestParam(required = false) Integer seatNo1, 
            @RequestParam(required = false) Integer seatNo2,
            HttpServletRequest request,
            Model model) throws Exception {

    	
    	JSONParser parser = new JSONParser();
    	JSONObject requestData = (JSONObject) parser.parse(jsonBody);
        
        String orderId = (String) requestData.get("orderId");
        String amountStr = (String) requestData.get("amount");
       

        if (amountStr == null || amountStr.trim().isEmpty()) {
            throw new IllegalArgumentException("결제 금액이 필요합니다.");
        }

        // 공백 제거
        amountStr = amountStr.trim();

        Long amount;
        try {
            amount = Long.parseLong(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("결제 금액이 유효한 숫자가 아닙니다.");
        }
        
        // 결제 승인 요청 전, 결제 결과의 orderId, amount 값이 처음 임시로 등록했던 값과 일치하는 지 검증
        paymentService.verifyPayment(orderId, amount);
    	
    	// 결제 진행 중 페이지 보기 위해 임의로 지연
    	Thread.sleep(1000);
    	
    	// 결제 승인 요청
        JSONObject response = paymentService.confirmPayment(jsonBody);

        boolean isSuccess = "DONE".equals(response.get("status"));
        
        if (!isSuccess) {
            return ResponseEntity.badRequest().body(response);
        }

        Payment payment = paymentService.updatePaymentEntity(response, showNo, seatNo1, seatNo2, showDate, member);
        
        response.put("paymentId", payment.getPaymentId());
        
        return ResponseEntity.ok(response);
    }
    
    
    
    @GetMapping("/approve/success")
    public String showConfirmationPage(
    		@RequestParam Long paymentId,
    		Model model) {
        Optional<Payment> payment = paymentService.getPaymentById(paymentId);
        model.addAttribute("payment", payment.orElseThrow(() -> new IllegalArgumentException("결제 정보가 존재하지 않습니다.")));
        return "/payment/success";
    }
    
    
    // 결제 승인 실패 시, 실패 페이지로 이동 (에러 메시지 전달하기)
    @GetMapping("/approve/fail")
    public String toApproveFailPage(@SessionAttribute("login") Member member) {
    	return "redirect:/payment/fail";
    }
    
    

    
    // 실패 페이지로 리디렉션 가능하도록 미리 매핑 설정
    @GetMapping("/fail")
    public String failPayment(
    		HttpServletRequest request,
    		Model model, 
    		@SessionAttribute("login") Member member) {
    	
        model.addAttribute("message", request.getParameter("message"));
        return "/payment/fail";
    }
    
    
    @GetMapping("/error")
    public String toErrorPage() {
    	return "/payment/error";
    }
}
