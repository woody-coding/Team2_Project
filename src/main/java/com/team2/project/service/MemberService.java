package com.team2.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team2.project.DTO.FindDTO;
import com.team2.project.DTO.LoginDTO;
import com.team2.project.model.Member;
import com.team2.project.repository.MemberRepository;

@Service
public class MemberService {
	@Autowired
	public MemberRepository memberRepo;

	public Optional<Member> getMemberLoginCheck(LoginDTO dto) {
		return memberRepo.findByMemberIdAndMemberPw(dto.getID(),dto.getPassword());
	}
	
	public Optional<Member> FindId(FindDTO dto) {
		return memberRepo.findBymemberNameAndMemberPhone(dto.getMemberName(),dto.getMemberPhone());
	}
	
	public Optional<Member> FindIdPw(FindDTO dto) {
		return memberRepo.findByMemberIdAndMemberNameAndMemberPhone(dto.getMemberId(),dto.getMemberName(),dto.getMemberPhone());
	}
	
	public Optional<Member> idchk(String id) {
		return memberRepo.findByMemberId(id);
	}
}