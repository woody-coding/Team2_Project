package com.team2.project.controller;

import com.team2.project.DTO.ShowDetailDTO;
import com.team2.project.model.Review;
import com.team2.project.model.Show;
import com.team2.project.model.ShowActor;
import com.team2.project.model.ShowActorFile;
import com.team2.project.service.ShowDetailService;
import com.team2.project.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/show")
public class ShowDetailController {

    @Autowired
    private ShowDetailService showService;
    
    @Autowired
    private ReviewService reviewService;
    

    // 공연 상세 페이지
    @GetMapping("/ticketDetailPage/{showNo}")
    public String ticketDetailPage(
            @PathVariable("showNo") int showNo, Model model) {

        Optional<ShowDetailDTO> optionalShowDTO = showService.getShowDetail(showNo);

        optionalShowDTO.ifPresent(showDTO -> model.addAttribute("show", showDTO));

        // 해당 공연에 출연하는 배우들 정보 가져오기
        List<ShowActor> actors = showService.getActorsByShowNo(showNo);
        

        // 각 배우에 대해 파일 정보를 추가
        actors.forEach(actor -> {
            String fileNo = actor.getActor().getFileNo(); // Actor의 fileNo 가져오기
            ShowActorFile actorFile = showService.getActorFileByFileNo(fileNo);
            actor.getActor().setShowActorFile(actorFile); // Actor에 파일 정보 설정
        });
        
        
        model.addAttribute("actors", actors);

        // 해당 공연(showNo)에 대한 리뷰 목록 가져오기 (내림차순 정렬)
        List<Review> reviews = reviewService.getReviewsByShowNo(showNo);
        model.addAttribute("reviews", reviews);

        model.addAttribute("showNo", showNo); // showNo 전달 (예매하기 페이지로..)

        return "ticket/ticketDetailpage/ticket_detailpage";
        //http://localhost:8787/show/ticketDetailPage/1
        
    }
    

    // 공연 목록 페이지 (카테고리 필터링 추가)
    @GetMapping({"/ticketMenu", "/ticketMenu/{showCate}"})
    public String ticketMenu(
            @PathVariable(value = "showCate", required = false) String showCate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("showNo").ascending());
        Page<ShowDetailDTO> showPage;

        if (showCate != null) {
            showPage = showService.getShowListByCategory(showCate, pageable);
            model.addAttribute("showCate", showCate);
        } else {
            showPage = showService.getShowList(pageable);
            model.addAttribute("showCate", "전체");
        }
        
     // 각 공연에 대한 평균 평점 계산 및 DTO에 추가
        showPage.getContent().forEach(show -> {
            double averageScore = reviewService.getAverageScoreByShowNo(show.getShowNo());
            show.setAverageScore(averageScore); // DTO에 averageScore 필드 필요
        });
        
        model.addAttribute("shows", showPage.getContent());
        model.addAttribute("currentPage", showPage.getNumber());
        model.addAttribute("totalPages", showPage.getTotalPages());
        model.addAttribute("hasNext", showPage.hasNext());
        model.addAttribute("hasPrevious", showPage.hasPrevious());

        return "ticket/ticketMenu/ticket_menu";
        //http://localhost:8787/show/ticketMenu
    }
    
    @GetMapping("/search")
    public String searchShows(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("showNo").ascending());
        List<Show> searchList = showService.searchShowList(keyword, pageable);

        List<ShowDetailDTO> showDetailDTOs = new ArrayList<>();
        for (Show show : searchList) {
            ShowDetailDTO dto = convertToShowDetailDTO(show);
            double averageScore = reviewService.getAverageScoreByShowNo(show.getShowNo());
            dto.setAverageScore(averageScore);
            showDetailDTOs.add(dto);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), showDetailDTOs.size());
        Page<ShowDetailDTO> showPage = new PageImpl<>(showDetailDTOs.subList(start, end), pageable, showDetailDTOs.size());

        model.addAttribute("shows", showPage.getContent());
        model.addAttribute("currentPage", showPage.getNumber());
        model.addAttribute("totalPages", showPage.getTotalPages());
        model.addAttribute("hasNext", showPage.hasNext());
        model.addAttribute("hasPrevious", showPage.hasPrevious());
        model.addAttribute("showCate", "전체");
        model.addAttribute("keyword", keyword);

        return "ticket/ticketMenu/ticket_menu";
    }

    private ShowDetailDTO convertToShowDetailDTO(Show show) {
        ShowDetailDTO dto = new ShowDetailDTO();
        dto.setShowNo(show.getShowNo());
        dto.setEndDate(show.getEndDate());
        dto.setOpenDate(show.getOpenDate());
        dto.setShowCate(show.getShowCate());
        dto.setShowInfo(show.getShowInfo());
        dto.setShowPlayTime(show.getShowPlayTime());
        dto.setShowPrice(show.getShowPrice());
        dto.setShowRating(show.getShowRating());
        dto.setShowTitle(show.getShowTitle());
        dto.setStartDate(show.getStartDate());
        dto.setShowActorFile(show.getShowActorFile());
        dto.setFileNo(show.getFileNo());
        return dto;
    }


    
    
}