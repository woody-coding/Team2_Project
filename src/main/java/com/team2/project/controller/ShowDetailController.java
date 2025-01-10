package com.team2.project.controller;

import com.team2.project.DTO.ShowDetailDTO;
import com.team2.project.model.Review;
import com.team2.project.model.ShowActor;
import com.team2.project.service.ShowDetailService;
import com.team2.project.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("actors", actors);   
        
     // 해당 공연(showNo)에 대한 리뷰 목록 가져오기
        List<Review> reviews = reviewService.getReviewsByShowNo(showNo);
        model.addAttribute("reviews", reviews);
        
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
    
    
    
    
    
}