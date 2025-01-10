package com.team2.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team2.project.model.LikeYO;

@Repository
public interface LikeYoRepository extends JpaRepository<LikeYO, Integer>{
	List<LikeYO> findByMemberNo(int memberNo);
	
	@Query("SELECT ly FROM LikeYO ly WHERE ly.memberNo = :memberNo")
    List<LikeYO> findLikedShowsAndActorsByMemberNo(@Param("memberNo") int memberNo);
    
	int countByMemberNoAndActorNotNull(int memberNo);
}
