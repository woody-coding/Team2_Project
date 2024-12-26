package com.team2.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team2.project.model.Member;

@Repository
public interface AdminRepository extends JpaRepository<Member, Integer> {
    // 모든 회원 리스트 조회
    List<Member> findAll();
}