package com.team2.project.repository;


import org.springframework.stereotype.Repository;

import com.team2.project.model.Inquiry;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
	
	// 해당 회원의 모든 문의 내역 조회
	List<Inquiry> findByMember_MemberNo(int memberNo);
	
	// 해당 회원의 특정 문의 글 삭제
	@Modifying
	@Query("DELETE FROM Inquiry "
			+ "i WHERE i.inquiryNo = :inquiryNo "
			+ "AND "
			+ "i.member.memberNo = :memberNo")
	void deleteByMemberAndInquiry(
			@Param("memberNo") int memberNo, 
			@Param("inquiryNo") int inquiryNo);

	
	// 날짜 범위 데이터 받아서 리스트 뽑아주기
	@Query("SELECT i FROM Inquiry i " +
		       "WHERE i.member.memberNo = :memberNo " +
		       "AND i.inquiryDate BETWEEN :startDate AND :endDate " +
		       "ORDER BY i.inquiryDate DESC")
		List<Inquiry> findAllByDateRange(
		    @Param("memberNo") int memberNo,
		    @Param("startDate") LocalDate startDate,
		    @Param("endDate") LocalDate endDate);


	// 관리자 답변 생성
    @Modifying
    @Query("UPDATE Inquiry i " +
           "SET i.inquiryAnswer = :inquiryAnswer, " +
           "i.inquiryAnswerDate = CURRENT_DATE, " +
           "i.inquiryStatus = 'ANSWER_CHOSEN' " +
           "WHERE i.inquiryNo = :inquiryNo")
    void updateByAdmin(
        @Param("inquiryNo") int inquiryNo,
        @Param("inquiryAnswer") String inquiryAnswer);
	
}
