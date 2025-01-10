package com.team2.project.repository;

import com.team2.project.model.ShowActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowActorRepository extends JpaRepository<ShowActor, Integer> {
    List<ShowActor> findByShowNo(int showNo);
}
