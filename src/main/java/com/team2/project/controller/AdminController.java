package com.team2.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.team2.project.model.Member;
import com.team2.project.model.Show;
import com.team2.project.service.AdminService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 회원 리스트 조회 페이지 (수동 페이징 처리)
    @GetMapping("/memberList")
    public String listMembers(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        List<Member> allMembers = adminService.getAllMembers(); // 모든 회원 리스트 조회
        System.out.println("Fetched members: " + allMembers); // 로그 추가

        int totalMembers = allMembers.size(); // 총 회원 수
        int totalPages = (int) Math.ceil((double) totalMembers / size); // 총 페이지 수

        // 현재 페이지의 시작과 끝 인덱스 계산
        int start = page * size;
        int end = Math.min(start + size, totalMembers);

        // 현재 페이지의 회원 리스트
        List<Member> members = allMembers.subList(start, end);

        model.addAttribute("members", members); // 현재 페이지의 회원 리스트
        model.addAttribute("currentPage", page); // 현재 페이지 번호
        model.addAttribute("totalPages", totalPages); // 전체 페이지 수
        model.addAttribute("totalMembers", totalMembers); // 총 회원 수
        return "admin/adminUserList"; // 회원 리스트를 보여줄 뷰 이름
    }
    
    
    
    //공연 조회
    @GetMapping("/showList")
    public String ShowList(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        List<Show> allShows = adminService.getAllShows();
        System.out.println("Fetched shows: " + allShows); // 로그 추가

        int totalShows = allShows != null ? allShows.size() : 0; // Null 체크
        int totalPages = (int) Math.ceil((double) totalShows / size);

        // 현재 페이지의 시작과 끝 인덱스 계산
        int start = page * size;
        int end = Math.min(start + size, totalShows);

        // 유효성 검사
        if (start >= totalShows) {
            start = totalShows; // 시작 인덱스가 총 쇼 수보다 크면 조정
        }

        List<Show> shows = allShows.subList(start, end);

        model.addAttribute("shows", shows);
        model.addAttribute("currentPage", page); // 현재 페이지 번호
        model.addAttribute("totalPages", totalPages); // 전체 페이지 수
        model.addAttribute("totalShows", totalShows);

        return "admin/adminShowList"; // 뷰 이름
    }
}
