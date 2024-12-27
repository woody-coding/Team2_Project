package com.team2.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team2.project.model.Member;

public interface Memberdao extends JpaRepository<Member, Integer>{
	
	@SuppressWarnings("unchecked")
	public Member save(Member dto);
}
