package com.team2.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.team2.project.DTO.AdminShowDTO;
import com.team2.project.model.Member;
import com.team2.project.model.Show;
import com.team2.project.model.ShowActorFile;
import com.team2.project.service.AdminService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

/*    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private AdminService adminService;

    *//**
     * InitBinder: 날짜 포맷 설정
     *//*
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, "startDate", new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(Date.class, "endDate", new CustomDateEditor(dateFormat, true));
    }

    *//**
     * 회원 리스트 조회 (수동 페이징 처리)
     *//*
    @GetMapping("/memberList")
    public String listMembers(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size) {
        List<Member> allMembers = adminService.getAllMembers();
        int totalMembers = allMembers.size();
        int totalPages = (int) Math.ceil((double) totalMembers / size);

        int start = page * size;
        int end = Math.min(start + size, totalMembers);

        List<Member> members = allMembers.subList(start, end);

        model.addAttribute("members", members);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalMembers", totalMembers);
        return "admin/adminUserList";
    }

    *//**
     * 공연 리스트 조회 (수동 페이징 처리)
     *//*
    @GetMapping("/showList")
    public String showList(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "20") int size) {
        List<Show> allShows = adminService.getAllShows();
        int totalShows = allShows != null ? allShows.size() : 0;
        int totalPages = (int) Math.ceil((double) totalShows / size);

        int start = page * size;
        int end = Math.min(start + size, totalShows);

        List<Show> shows = allShows.subList(start, end);

        model.addAttribute("shows", shows);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalShows", totalShows);
        return "admin/adminShowList";
    }

    *//**
     * 공연 상세 조회
     *//*
    @GetMapping("/showDetail/{showNo}")
    public String showDetail(@PathVariable int showNo, Model model) {
        Show show = adminService.getShowById(showNo);
        if (show != null) {
            model.addAttribute("show", show);
            return "admin/adminShowDetail";
        } else {
            return "error/404"; // 공연이 없을 경우 에러 페이지로 이동
        }
    }

    *//**
     * 공연 수정 페이지 이동
     *//*
    @GetMapping("/adminShowUpdate")
    public String showUpdate(@RequestParam int showNo, Model model) {
        Show show = adminService.getShowById(showNo);
        if (show != null) {
            model.addAttribute("show", show);
            return "admin/adminShowUpdate";
        } else {
            return "error/404";
        }
    }

    *//**
     * 공연 수정 처리
     *//*
    @PostMapping("/updateShow")
    public String updateShow(@ModelAttribute Show show) {
        adminService.updateShow(show);
        return "redirect:/admin/showList";
    }

    *//**
     * 공연 등록 폼
     *//*
    @GetMapping("/showInsert")
    public String showInsertForm(Model model) {
        AdminShowDTO adminShowDTO = new AdminShowDTO();
        model.addAttribute("adminShowDTO", adminShowDTO);
        return "admin/adminShowInsert"; // 입력 페이지 이름
    }

    *//**
     * 공연 등록 처리
     *//*
    @PostMapping("/showInsert")
    public String insertShow(@ModelAttribute AdminShowDTO adminShowDTO,
                             @RequestParam("showImage") MultipartFile file,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("message", "입력값에 오류가 있습니다.");
            return "admin/adminShowInsert"; // 오류가 있을 경우 다시 폼으로 돌아감
        }
        
        if (adminShowDTO.getShowCate() == null || adminShowDTO.getShowCate().isEmpty()) {
            model.addAttribute("message", "공연 카테고리를 선택해야 합니다.");
            return "admin/adminShowInsert";
        }

        // Show 엔티티 생성
        Show show = new Show();
        show.setShowNo(adminShowDTO.getShowNo());
        show.setShowPrice(adminShowDTO.getShowPrice());
        show.setStartDate(adminShowDTO.getStartDate());
        show.setEndDate(adminShowDTO.getEndDate());
        show.setShowTitle(adminShowDTO.getShowTitle());
        show.setShowInfo(adminShowDTO.getShowInfo());
        show.setTotSeat(adminShowDTO.getTotSeat());
        show.setLeftSeat(adminShowDTO.getLeftSeat());
        show.setShowCate(adminShowDTO.getShowCate());
        show.setShowTime(adminShowDTO.getShowTime());
        show.setShowPlace(adminShowDTO.getShowPlace());
        show.setShowPlayTime(adminShowDTO.getShowPlayTime());
        show.setShowRating(adminShowDTO.getShowRating());
        
        // Show 엔티티 저장
        adminService.insertShow(show);

        // 파일 업로드 처리
        if (!file.isEmpty()) {
            try {
                String fileName = saveUploadedFile(file);
                ShowActorFile showActorFile = new ShowActorFile();
                showActorFile.setFilePath(Paths.get(uploadDir, fileName).toString());
                showActorFile.setFileName(fileName);
                showActorFile.setShow(show);
                adminService.saveShowActorFile(showActorFile, show, null);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "파일 업로드 중 오류가 발생했습니다.");
                return "admin/adminShowInsert";
            }
        }

        model.addAttribute("message", "공연이 성공적으로 등록되었습니다.");
        return "redirect:/admin/showList";
    }

    *//**
     * 파일 저장 처리
     *//*
    private String saveUploadedFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath.toFile());
        return fileName;
    }*/
}
