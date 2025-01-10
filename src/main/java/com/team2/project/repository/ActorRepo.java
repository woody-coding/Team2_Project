package com.team2.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team2.project.model.Actor;

@Repository
public interface ActorRepo extends JpaRepository<Actor, Integer>{
	
	public Actor findByActorNo(int actorNo);
}
