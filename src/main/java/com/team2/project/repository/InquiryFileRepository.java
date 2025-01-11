package com.team2.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.team2.project.model.Inquiry;
import com.team2.project.model.InquiryFile;

public interface InquiryFileRepository extends JpaRepository<InquiryFile, String> {

	List<InquiryFile> findByInquiry(Inquiry inquiry);
	
	@Query("DELETE FROM InquiryFile f WHERE f.inquiryFileNo = :inquiryFileNo")
	@Modifying
	@Transactional
	void deleteById(String inquiryFileNo);
	
}