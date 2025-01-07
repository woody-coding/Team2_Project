package com.team2.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team2.project.model.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>{

	@SuppressWarnings("unchecked")
	public Member save(Member dto);
	
	Optional<Member> findByMemberIdAndMemberPw(String Id,String Password);
	
	Optional<Member> findBymemberNameAndMemberPhone(String Name,String Phone);
	
	Optional<Member> findByMemberIdAndMemberNameAndMemberPhone(String Id,String Password,String Phone);
	
	Optional<Member> findByMemberId(String id);
}
