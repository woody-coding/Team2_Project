package com.team2.project.repository;

import java.util.List;

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
    
    // 공연 제목, 정보를 바탕으로 검색 => 공연 리스트 뽑기
    List<Show> findByShowTitleContainingOrShowInfoContaining(String titleKeyword, String infoKeyword, Pageable pageable);
}