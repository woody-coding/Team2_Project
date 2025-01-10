package com.team2.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.team2.project.model.Show;

@Repository
public interface ShowDetailRepository extends JpaRepository<Show, Integer> {
    Show findByShowNo(int showNo);

    // 카테고리별 조회 메서드
    Page<Show> findByShowCate(String showCate, Pageable pageable);
}