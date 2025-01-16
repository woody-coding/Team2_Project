package com.team2.project.service;

import com.team2.project.model.Actor;
import com.team2.project.model.ShowActor;
import com.team2.project.model.ShowActorFile;
import com.team2.project.repository.ShowActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShowActorService {

    @Autowired
    private ShowActorRepository showActorRepository;

    public List<ShowActor> getActorsByShowNo(int showNo) {
        // showNo로 ShowActor 목록을 조회
        List<ShowActor> showActors = showActorRepository.findByShowNo(showNo);

        // ShowActor 목록을 순회하며 각 ShowActorFile의 filePath를 처리
        for (ShowActor showActor : showActors) {
            // showActor에서 ShowActorFile 가져오기
            if (showActor.getActor() != null && showActor.getActor().getShowActorFile() != null) {
                ShowActorFile showActorFile = showActor.getActor().getShowActorFile();
                String filePath = showActorFile.getFilePath();
                // filePath를 ShowActor에 설정하거나 반환에 포함
                // 예: showActor.setFilePath(filePath); // 필요시 추가
            }
        }

        return showActors;
    }


    @Transactional
    public void saveShowActor(ShowActor showActor) {
        showActorRepository.save(showActor);
    }
}
