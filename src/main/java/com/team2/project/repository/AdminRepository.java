package com.team2.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.team2.project.model.Member;
import com.team2.project.model.Show;

@Repository
public interface AdminRepository extends JpaRepository<Member, Integer> {
    // 모든 회원 리스트 조회
    List<Member> findAll();
    
    @Query("SELECT s FROM Show s") // Show 엔티티를 가져오는 JPQL 쿼리
    List<Show> findAllShow();
    
    // ID로 Show 조회
    @Query("SELECT s FROM Show s WHERE s.showNo = ?1")
    Show findShowById(int showNo);
    
    <S extends Show> S save(S entity);
    
}