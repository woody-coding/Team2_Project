package com.team2.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.team2.project.model.Member;
import com.team2.project.model.Show;
import com.team2.project.repository.AdminRepository;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    // 모든 회원 리스트 조회
    public List<Member> getAllMembers() {
        return adminRepository.findAll(); // 모든 회원 리스트 조회
    }
    
    public List<Show> getAllShows(){
    	return adminRepository.findAllShow();
    }
}
