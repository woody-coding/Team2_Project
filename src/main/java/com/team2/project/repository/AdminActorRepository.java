package com.team2.project.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team2.project.model.Actor;

@Repository
public interface AdminActorRepository extends JpaRepository<Actor, Integer>{

	public List<Actor> findAll();
	
	public List<Actor> findAll(Sort sort);
	
	public Actor save(Actor actor);
	
	public Actor findByActorNo(int actorNo);
	
	
	
}
