package com.team2.project.service;

import com.team2.project.DTO.ShowDetailDTO;
import com.team2.project.model.Show;
import com.team2.project.model.ShowActor;
import com.team2.project.model.ShowActorFile;
import com.team2.project.repository.ShowDetailRepository;
import com.team2.project.repository.ShowActorFileRepository;
import com.team2.project.repository.ShowActorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ShowDetailService {

    @Autowired
    private ShowDetailRepository showRepository;
    
    @Autowired
    private ShowActorRepository showActorRepository;
    
    @Autowired
    private ShowActorFileRepository showActorFileRepository;
    
    //배우 이미지파일 업로드처리
    public ShowActorFile getActorFileByFileNo(String fileNo) {
        return showActorFileRepository.findById(fileNo).orElse(null);
    }
    
    //상세페이지 
    public Optional<ShowDetailDTO> getShowDetail(int showNo) {
        return Optional.ofNullable(showRepository.findByShowNo(showNo))
                .map(this::convertToDTO);
    }
    
    //상세페이지 ShowNo에 해당하는 배우 리스트 불러오기
    public List<ShowActor> getActorsByShowNo(int showNo) {
        return showActorRepository.findByShowNo(showNo);
    }
    
    //뮤지컬, 연극 리스트 페이지
    public Page<ShowDetailDTO> getShowList(Pageable pageable) {
        return showRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    //뮤지컬, 연극 리스트 페이지 카테고리별 조회 기능 구현
    public Page<ShowDetailDTO> getShowListByCategory(String showCate, Pageable pageable) {
        return showRepository.findByShowCate(showCate, pageable)
                .map(this::convertToDTO);
    }
    
    public List<Show> searchShowList(String keyword, Pageable pageable) {
        if(keyword == null) keyword = "";
        
        return showRepository.findByShowTitleContainingOrShowInfoContaining(keyword, keyword, pageable);
    }


    private ShowDetailDTO convertToDTO(Show show) {
        ShowDetailDTO dto = new ShowDetailDTO();
        dto.setShowNo(show.getShowNo());
        dto.setShowPrice(show.getShowPrice());
        dto.setStartDate(show.getStartDate());
        dto.setEndDate(show.getEndDate());
        dto.setShowTitle(show.getShowTitle());
        dto.setShowInfo(show.getShowInfo());
        dto.setShowCate(show.getShowCate());
        dto.setShowStartTime(show.getShowStartTime());
        dto.setShowPlayTime(show.getShowPlayTime());
        dto.setShowRating(show.getShowRating());
        dto.setOpenDate(show.getOpenDate());
        return dto;
    }
    
    
}