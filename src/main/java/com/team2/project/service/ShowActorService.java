package com.team2.project.service;

import com.team2.project.model.ShowActor;
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
        return showActorRepository.findByShowNo(showNo);
    }

    @Transactional
    public void saveShowActor(ShowActor showActor) {
        showActorRepository.save(showActor);
    }
}
