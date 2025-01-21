package com.team2.project.service;

import com.team2.project.model.Show;
import com.team2.project.model.ShowActorFile;
import com.team2.project.model.Actor;
import com.team2.project.model.Member;
import com.team2.project.repository.AdminRepository;
import com.team2.project.repository.ShowActorFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ShowActorFileRepository showActorFileRepository;

    // 모든 회원 리스트 조회
    public List<Member> getAllMembers() {
        return adminRepository.findAll(); // 모든 회원 리스트 조회
    }
    
    public List<Show> getAllShows() {
        return adminRepository.findAllShow(Sort.by(Sort.Direction.DESC, "showNo"));
    }
    
    public Show getShowById(int ShowNo) {
    	return adminRepository.findShowById(ShowNo);
    }
    
    public void updateShow(Show show) {
        adminRepository.save(show);
    }

    @Transactional
    public void insertShow(Show show) {
        adminRepository.save(show);
    }

    @Transactional
    public void saveShowActorFile(ShowActorFile showActorFile, Show show, Actor actor) {
        // Show와 Actor가 이미 저장되어 있어야 함
        showActorFile.setShow(show); // Show 엔티티 설정
        showActorFile.setActor(actor); // Actor 엔티티 설정
        showActorFileRepository.save(showActorFile); // ShowActorFile 저장
    }
}
