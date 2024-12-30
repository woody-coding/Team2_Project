package com.team2.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team2.project.model.Actor;
import com.team2.project.model.ShowActorFile;
import com.team2.project.repository.AdminActorRepository;
import com.team2.project.repository.ShowActorFileRepository;

@Service
public class AdminActorService {
	
	@Autowired
	private AdminActorRepository adminActorRepository;
	
	@Autowired
	private ShowActorFileRepository showActorFileRepository;
	
	
	public List<Actor> findAllActor(Sort sort){
		return adminActorRepository.findAll(sort);
	}
	
	public Actor getActorByActorNo(int actorNo) {
        return adminActorRepository.findByActorNo(actorNo);
	}
	@Transactional
    public ShowActorFile saveShowActorFile(ShowActorFile showActorFile) {
        return showActorFileRepository.save(showActorFile); // showActorFileRepository는 JPA Repository
    }

	public Actor saveActor(Actor actor) {
		return adminActorRepository.save(actor);
	}
}
