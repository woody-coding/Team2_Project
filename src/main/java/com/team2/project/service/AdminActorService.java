package com.team2.project.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	
	public ShowActorFile getShowActorFileByActorNo(int actorNo) {
		Actor actor = getActorByActorNo(actorNo);
		if (actor != null) {
			return showActorFileRepository.findByActor(actor);
		}
		return null; // 배우가 존재하지 않는 경우 null 반환
	}
	
	@Transactional
	public void deleteShowActorFile(ShowActorFile showActorFile) {
	    showActorFileRepository.delete(showActorFile);
	}
	
	public void deleteExistingFile(Actor existingActor) {
	    String fileNo = existingActor.getShowActorFile().getFileNo(); // fileNo가 String 타입
	    ShowActorFile existingFile = showActorFileRepository.findById(fileNo).orElse(null);
	    if (existingFile != null) {
	        Path existingFilePath = Paths.get(existingFile.getFilePath());
	        try {
	            Files.deleteIfExists(existingFilePath); // 로컬 파일 시스템에서 삭제
	            deleteShowActorFile(existingFile); // DB에서 삭제
	        } catch (IOException e) {
	            e.printStackTrace(); // 예외 처리
	        }
	    }
	}
	
	
}
