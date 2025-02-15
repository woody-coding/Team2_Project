package com.team2.project.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.team2.project.model.Member;
import com.team2.project.model.Show;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public interface AdminRepository extends JpaRepository<Member, Integer> {
    // 모든 회원 리스트 조회
    List<Member> findAll();
    
    @Query("SELECT s FROM Show s") // Show 엔티티를 가져오는 JPQL 쿼리
    List<Show> findAllShow(Sort sort);
    
    // ID로 Show 조회
    @Query("SELECT s FROM Show s WHERE s.showNo = ?1")
    Show findShowById(int showNo);
    
    <S extends Show> S save(S entity);
   
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO MULTI24_SHOW (SHOW_PRICE, START_DATE, END_DATE, SHOW_TITLE, SHOW_INFO, TOT_SEAT, LEFT_SEAT, SHOW_CATE, SHOW_TIME, SHOW_PLACE, SHOW_PLAYTIME, SHOW_RATING, FILE_NO) " +
                   "VALUES (:showPrice, :startDate, :endDate, :showTitle, :showInfo, :totSeat, :leftSeat, :showCate, :showTime, :showPlace, :showPlayTime, :showRating, :fileNo)", 
           nativeQuery = true)
    void saveShow(String showPrice, Date startDate, Date endDate, String showTitle, String showInfo, int totSeat, int leftSeat, String showCate, Date showTime, String showPlace, int showPlayTime, String showRating, String fileNo);
}
