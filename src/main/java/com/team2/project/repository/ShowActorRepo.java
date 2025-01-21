package com.team2.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team2.project.model.ShowActor;

@Repository
public interface ShowActorRepo extends JpaRepository<ShowActor, Integer> {
	
	public List<ShowActor> findTop3ByActorNo(int actorNo);
	
}
