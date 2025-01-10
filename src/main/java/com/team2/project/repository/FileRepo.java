package com.team2.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team2.project.model.Actor;
import com.team2.project.model.Show;
import com.team2.project.model.ShowActorFile;

@Repository
public interface FileRepo extends JpaRepository<ShowActorFile, String> {

	public ShowActorFile findByShow(Show show);

	public ShowActorFile findByActor(Actor actor);

}
