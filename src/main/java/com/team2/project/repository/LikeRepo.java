package com.team2.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team2.project.model.LikeYO;

@Repository
public interface LikeRepo extends JpaRepository<LikeYO, Integer> {
	
	public Optional<LikeYO> findByMemberNoAndActorNo(int memberNo, int actorNo);
}
