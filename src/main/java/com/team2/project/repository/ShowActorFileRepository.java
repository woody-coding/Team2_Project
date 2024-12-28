package com.team2.project.repository;

import com.team2.project.model.ShowActorFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowActorFileRepository extends JpaRepository<ShowActorFile, String> {
    // 기본적인 CRUD 메서드는 JpaRepository가 제공
} 