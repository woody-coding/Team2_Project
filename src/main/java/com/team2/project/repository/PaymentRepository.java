package com.team2.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team2.project.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	public Payment findByOrderId(String orderId);
}

