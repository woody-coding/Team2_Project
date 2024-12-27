package com.team2.project.repository;

import org.springframework.stereotype.Repository;

import com.team2.project.model.Inquiry;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
	
	// 날짜 범위 데이터 받아서 리스트 뽑아주기
	@Query("SELECT i FROM Inquiry i " +
		       "WHERE i.member.memberNo = :memberNo " +
		       "AND (i.inquiryDate >= :startDate) " +
		       "AND (i.inquiryDate <= :endDate)")
	List<Inquiry> findAllByDateRange(
		    @Param("memberNo") int memberNo,
		    @Param("startDate") LocalDate startDate,
		    @Param("endDate") LocalDate endDate,
			Sort sort);

	// 관리자 답변 생성
    @Query("UPDATE Inquiry i " +
           "SET i.inquiryAnswer = :inquiryAnswer, " +
           "i.inquiryAnswerDate = CURRENT_DATE, " +
           "i.inquiryStatus = 'ANSWER_CHOSEN' " +
           "WHERE i.inquiryNo = :inquiryNo")
    public int updateByAdmin(
        @Param("inquiryNo") int inquiryNo,
        @Param("inquiryAnswer") String inquiryAnswer);
	
}
