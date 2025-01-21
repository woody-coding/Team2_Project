package com.team2.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import com.team2.project.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	public Payment findByOrderId(String orderId);

	//민영 추가 : review작성 결제자만 가능하도록 처리
	Optional<Payment> findByMember_MemberNoAndOrderNameAndPaySuccessYN(int memberNo, String orderName, boolean paySuccessYN);
	
	List<Payment> findByMember_MemberNoAndPaySuccessYNTrue(int memberNo);
}

