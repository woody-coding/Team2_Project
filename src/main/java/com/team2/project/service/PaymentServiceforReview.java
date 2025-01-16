package com.team2.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team2.project.model.Payment;
import com.team2.project.repository.PaymentRepository;

@Service
public class PaymentServiceforReview {
    
    @Autowired
    private PaymentRepository paymentRepository;

    public Optional<Payment> findByMemberNoAndOrderNameAndPaySuccessYN(int memberNo, String orderName, boolean paySuccessYN) {
        return paymentRepository.findByMember_MemberNoAndOrderNameAndPaySuccessYN(memberNo, orderName, paySuccessYN);
    }
}