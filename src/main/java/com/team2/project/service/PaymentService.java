package com.team2.project.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team2.project.DTO.OrderDetailsDTO;
import com.team2.project.DTO.ShowTitleAndPriceDTO;
import com.team2.project.model.Member;
import com.team2.project.model.Payment;
import com.team2.project.model.Seat;
import com.team2.project.model.Show;
import com.team2.project.repository.PaymentRepository;
import com.team2.project.repository.SeatRepository;
import com.team2.project.repository.ShowRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService {
	
	private final PaymentRepository paymentRepository;
	private final SeatRepository seatRepository;
	private final ShowRepository showRepository;
	
	@Value("${API_SECRET_KEY}")
    private String API_SECRET_KEY;

    public JSONObject confirmPayment(String jsonBody) throws Exception {
    	JSONObject response = sendRequest(parseRequestData(jsonBody), API_SECRET_KEY, "https://api.tosspayments.com/v1/payments/confirm");
    	
    	return response;
    }


    // 역직렬화
    private JSONObject parseRequestData(String jsonBody) {
        try {
            return (JSONObject) new JSONParser().parse(jsonBody);
        } catch (ParseException e) {
            return new JSONObject();
        }
    }
    
    // 토스 결제 승인 API에 POST 방식으로 요청/응답 처리 메서드
    @Transactional
    private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
        HttpURLConnection connection = createConnection(secretKey, urlString);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
        }

        JSONObject response = new JSONObject();
        int responseCode = connection.getResponseCode();
        
        try (InputStream responseStream = (responseCode == 200) ? connection.getInputStream() : connection.getErrorStream();
             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            JSONObject jsonResponse = (JSONObject) new JSONParser().parse(reader);
            
            if (responseCode == 200) {
                response.put("status", "DONE");
                response.putAll(jsonResponse);
            } else {
                response.put("status", "FAILED");
                response.put("error", jsonResponse);
            }
        } catch (Exception e) {
            response.put("status", "FAILED");
            response.put("error", "Error reading response: " + e.getMessage());
        }

        return response;
    }


    // 토스 API와의 HTTP 연결을 위한 작업
    private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
    	URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }
    
    // 최종 결제 완료 후, 1) Seat 업데이트 + 2) Payment 업데이트
    @Transactional
    public Payment updatePaymentEntity(JSONObject response, int showNo, int seatNo1, int seatNo2, Date showDate, Member member) {
        // 값 확인을 위한 출력
        System.out.println("Response JSON: " + response.toJSONString());

        String paymentKey = (String) response.get("paymentKey");
        String orderId = (String) response.get("orderId");

        // totalAmount를 Number로 가져오기
        Number amountNumber = (Number) response.get("totalAmount");
        if (amountNumber == null || amountNumber.longValue() <= 0) {
            throw new IllegalArgumentException("결제 금액이 필요하고 0보다 커야 합니다.");
        }

        Long amount = amountNumber.longValue(); // Long으로 변환

        String orderName = (String) response.get("orderName");
        if (orderName == null || orderName.trim().isEmpty()) {
            throw new IllegalArgumentException("주문 이름이 필요합니다.");
        }

        String payType = (String) response.get("method");
        if (payType == null || payType.trim().isEmpty()) {
            throw new IllegalArgumentException("결제 유형이 필요합니다.");
        }

        Payment payment = paymentRepository.findByOrderId(orderId);
        
        if (payment == null) {
            throw new IllegalArgumentException("결제 정보를 찾을 수 없습니다: " + orderId);
        }
        
        
        
     // 1. 좌석 관계 설정
        List<Seat> seats = new ArrayList<>();

        // seatNo1 추가
        Seat seat1 = seatRepository.findByShowShowNoAndSeatNoAndShowDate(showNo, seatNo1, showDate)
                .orElseThrow(() -> new IllegalArgumentException("좌석 번호가 유효하지 않습니다: " + seatNo1));
        
        seat1.setPayment(payment);
        seat1.setIsBook("Y");
        seats.add(seat1);

        // seatNo2 처리
        if (seatNo2 != 0) { // int 타입이므로 null 체크 대신 0 체크
            Seat seat2 = seatRepository.findByShowShowNoAndSeatNoAndShowDate(showNo, seatNo2, showDate)
                    .orElseThrow(() -> new IllegalArgumentException("좌석 번호가 유효하지 않습니다: " + seatNo2));
            
            seat2.setPayment(payment);
            seat2.setIsBook("Y");
            seats.add(seat2);
        }

        // 모든 좌석 저장
        List<Seat> savedSeats = seatRepository.saveAll(seats);
        
        updateLeftSeats(showNo, showDate, seats.size());
        
        
        // 2. 결제 정보 업데이트
        payment.setShowTime(savedSeats.get(0).getShowTime());
        payment.setSeatSpaces(savedSeats.get(0).getSeatSpace());
        if(savedSeats.size() == 2) {
        	payment.setSeatSpaces(savedSeats.get(0).getSeatSpace() + ", " + savedSeats.get(1).getSeatSpace());
        }
        payment.setShowDate(showDate);
        payment.setAmount(amount);
        payment.setPaymentKey(paymentKey);
        payment.setPaySuccessYN(true);
        payment.setOrderName(orderName);
        payment.setPayType(payType);
        payment.setMemberName(member.getMemberName());

        if (payment.getMember().getMemberNo() != member.getMemberNo()) {
            throw new IllegalArgumentException("회원이 일치하지 않습니다.");
        }

        // 결제 정보 저장
        paymentRepository.save(payment);

        return payment;
    }


    @Transactional
    private void updateLeftSeats(int showNo, Date showDate, int bookedSeats) {
        // 해당 showNo와 showDate에 맞는 모든 좌석 조회
        List<Seat> matchingSeats = seatRepository.findByShowShowNoAndShowDate(showNo, showDate);

        for (Seat seat : matchingSeats) {
            int currentLeftSeats = seat.getLeftSeat();
            int updatedLeftSeats = currentLeftSeats - bookedSeats; // 예약된 좌석 수만큼 감소

            // leftSeat 값 업데이트
            seat.setLeftSeat(updatedLeftSeats);
            seatRepository.save(seat); // 변경된 좌석 저장
        }
    }
    


    
    // 결제 전 미리 임시 저장해둔 orderId, amount와 결제되었을 때 orderId, amount 값을 비교하여 검증
    @Transactional(readOnly = true)
    public Payment verifyPayment(String orderId, Long amount) {
        Payment savedPayment = paymentRepository.findByOrderId(orderId);
        
        if (savedPayment == null) {
            throw new IllegalArgumentException("주문 번호가 일치하지 않습니다 : " + orderId);
        }
        
        if (!savedPayment.getAmount().equals(amount)) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다. 저장된 금액: " + savedPayment.getAmount() + ", 요청 금액: " + amount);
        }
        
        return savedPayment;
    }

    
    // 결제 요청 전, 미리 orderId, amount 값 등록해두기
    @Transactional
    public void saveOrderDetails(OrderDetailsDTO orderDetails, Member member) {
        Payment payment = new Payment();
        payment.setMember(member);
        payment.setOrderId(orderDetails.getOrderId());
        payment.setAmount(orderDetails.getAmount());
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);
    }
    
    // 선택한 좌석 1개 or 2개가 모두 결제 가능한 상태인지 검증하는 메서드
    // 해당 메서드는 그냥 검증용으로 DB에서 읽기만 함으로, 불필요한 잠금을 방지하기 위해 readOnly 속성 추가
    @Transactional(readOnly = true)
    public boolean areSeatAvailable(int showNo, int seatNo1, int seatNo2, Date showDate) {
        Seat seat1 = seatRepository.findByShowShowNoAndSeatNoAndShowDate(showNo, seatNo1, showDate)
        		.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 좌석 번호 입니다 : " + seatNo1));
        
        if (seatNo2 != 0) {
            Seat seat2 = seatRepository.findByShowShowNoAndSeatNoAndShowDate(showNo, seatNo2, showDate)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 좌석 번호 입니다 : " + seatNo2));
            
            return "N".equals(seat1.getIsBook()) && "N".equals(seat2.getIsBook());
        }
        
        return "N".equals(seat1.getIsBook());
    }
    
    public ShowTitleAndPriceDTO getShowTitleAndPrice(int showNo) {
    	Show show = showRepository.findByShowNo(showNo);
    	
    	return new ShowTitleAndPriceDTO(show.getShowTitle(), show.getShowPrice());
    }
    
    public Optional<Payment> getPaymentById(Long paymentId) {
    	return paymentRepository.findById(paymentId);
    }


}
