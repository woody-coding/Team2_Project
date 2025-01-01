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

import com.team2.project.model.Show;
import com.team2.project.model.ShowActorFile;
import com.team2.project.repository.AdminShowRepository;
import com.team2.project.repository.ShowActorFileRepository;

@Service
public class AdminShowService {

	@Autowired
	AdminShowRepository adminShowRepository;
	
	@Autowired
	ShowActorFileRepository showActorFileRepository;
	
	
	public List<Show> findAllShow(Sort sort){
		return adminShowRepository.findAll(sort);
	}
	
	public Show getShowByShowNo(int showNo) {
        return adminShowRepository.findByShowNo(showNo);
	}
	@Transactional
    public ShowActorFile saveShowActorFile(ShowActorFile showActorFile) {
        return showActorFileRepository.save(showActorFile);
    }

	public Show saveShow(Show show) {
		return adminShowRepository.save(show);
	}
	
	public ShowActorFile getShowActorFileByShowNo(int showNo) {
		Show show = getShowByShowNo(showNo);
		if (show != null) {
			return showActorFileRepository.findByShow(show);
		}
		return null; // 공연이 존재하지 않는 경우 null 반환
	}
	
	@Transactional
	public void deleteShowActorFile(ShowActorFile showActorFile) {
	    showActorFileRepository.delete(showActorFile);
	}
	
	public void deleteExistingFile(Show existingShow) {
	    String fileNo = existingShow.getShowActorFile().getFileNo(); // fileNo가 String 타입
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
