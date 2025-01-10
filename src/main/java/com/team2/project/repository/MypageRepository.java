package com.team2.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team2.project.model.Member;

@Repository
public interface MypageRepository extends JpaRepository<Member, Integer>{
	
}
