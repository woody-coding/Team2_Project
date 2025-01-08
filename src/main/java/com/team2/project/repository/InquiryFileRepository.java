package com.team2.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team2.project.model.Inquiry;
import com.team2.project.model.InquiryFile;

public interface InquiryFileRepository extends JpaRepository<InquiryFile, String> {

	List<InquiryFile> findByInquiry(Inquiry inquiry);
	
}