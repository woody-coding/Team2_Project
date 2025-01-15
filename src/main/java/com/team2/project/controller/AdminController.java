package com.team2.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.team2.project.DTO.AdminShowDTO;
import com.team2.project.model.Actor;
import com.team2.project.model.Member;
import com.team2.project.model.Show;
import com.team2.project.model.ShowActorFile;
import com.team2.project.model.ShowActor;
import com.team2.project.repository.AdminActorRepository;
import com.team2.project.service.AdminActorService;
import com.team2.project.service.AdminService;
import com.team2.project.service.AdminShowService;
import com.team2.project.service.ShowActorService;

import retrofit2.http.POST;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Autowired
	private AdminService adminService;
	
	/**
	 * 파일 저장 처리
	 */
	private String saveUploadedFile(MultipartFile file) throws IOException {
	    // 애플리케이션 루트 디렉토리 기준으로 경로 설정
	    Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", "uploads");

	    // 디렉토리가 존재하지 않으면 생성
	    if (!Files.exists(uploadPath)) {
	        Files.createDirectories(uploadPath);
	    }

	    // 고유한 파일 이름 생성
	    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	    Path filePath = uploadPath.resolve(fileName);

	    // 파일 저장
	    file.transferTo(filePath.toFile());

	    return fileName;
	}
	

	/**
	 * InitBinder: 날짜 포맷 설정
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, "startDate", new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Date.class, "endDate", new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Date.class, "openDate", new CustomDateEditor(dateFormat, true));
	}

	/**
	 * 회원 리스트 조회 (수동 페이징 처리)
	 */
	@GetMapping("/memberList")
	public String listMembers(@SessionAttribute("login") Optional<Member> optionalMember, Model model, 
	                          @RequestParam(defaultValue = "0") int page,
	                          @RequestParam(defaultValue = "20") int size) {
	    // 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        // 모든 회원 정보 조회
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
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }

	    return "admin/adminUserList";
	}

	
	@Autowired
	private AdminShowService adminShowService;

	@Autowired
	private ShowActorService showActorService;

	/**
	 * 공연 리스트 조회 (수동 페이징 처리)
	 */
	@GetMapping("/showList")
	public String showList(@SessionAttribute("login") Optional<Member> optionalMember, Model model, 
	                       @RequestParam(defaultValue = "0") int page,
	                       @RequestParam(defaultValue = "20") int size) {
	    // 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
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
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }

	    return "admin/adminShowList";
	}


	/**
	 * 공연 상세 조회
	 */
	@GetMapping("/showDetail")
	public String showDetail(@SessionAttribute("login") Optional<Member> optionalMember, @RequestParam int showNo, Model model) {
	    // 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        try {
	            Show show = adminShowService.getShowByShowNo(showNo);
	            if (show != null) {
	                List<ShowActor> showActors = showActorService.getActorsByShowNo(showNo);
	                model.addAttribute("show", show);
	                model.addAttribute("showActors", showActors);
	                return "admin/adminShowDetail";
	            } else {
	                System.out.println("Show not found for showNo: " + showNo);
	                return "error/404";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "error/500";
	        }
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }
	}


	/**
	 * 공연 수정 페이지 이동
	 */
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

	/**
	 * 공연 수정 처리
	 */
	@PostMapping("/updateShow")
	public String updateShow(@SessionAttribute("login") Optional<Member> optionalMember, @ModelAttribute Show show) {
	    // 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        adminService.updateShow(show);
	        return "redirect:/admin/showList";
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }
	}

	
	

	/**
	 * 공연 등록 폼
	 */
	@GetMapping("/showInsertForm")
	public String showInsertForm(Model model){


		return "admin/adminShowInsert";
	}

	@PostMapping("/showInsert")
	public String showInsert(@SessionAttribute("login") Optional<Member> optionalMember, 
	                         @RequestParam("file") MultipartFile file, 
	                         @ModelAttribute Show show, 
	                         @RequestParam(value = "actorNo", required = false) List<Integer> actorNo, 
	                         @RequestParam(value = "roleName", required = false) List<String> roleName) {
	    // 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        try {
	            // 기존 공연 정보 저장 로직
	            if (show.getStartDate() != null) {
	                Calendar calendar = Calendar.getInstance();
	                calendar.setTime(show.getStartDate());
	                calendar.add(Calendar.DAY_OF_YEAR, -20);
	                Date openDate = calendar.getTime();
	                show.setOpenDate(openDate);
	            }

	            String fileName = saveUploadedFile(file);
	            String fileNo = fileName;

	            ShowActorFile showActorFile = new ShowActorFile();
	            showActorFile.setFileNo(fileNo);
	            showActorFile.setFilePath(Paths.get("src/main/resources/static/uploads", fileName).toString());
	            showActorFile.setFileName(file.getOriginalFilename());
	            showActorFile.setFileDate(new Date());

	            adminShowService.saveShow(show);
	            showActorFile.setShow(show);
	            adminShowService.saveShowActorFile(showActorFile);

	            show.setShowActorFile(showActorFile);
	            show.setFileNo(showActorFile.getFileNo());
	            adminShowService.saveShow(show);
	            adminShowService.createSeatsForShow(show);

	            // 새로운 ShowActor 정보 저장 로직 추가
	            if (actorNo != null && roleName != null && actorNo.size() == roleName.size()) {
	                for (int i = 0; i < actorNo.size(); i++) {
	                    if (actorNo.get(i) != null) { // null 값 체크
	                        ShowActor showActor = new ShowActor();
	                        showActor.setShowNo(show.getShowNo());
	                        showActor.setActorNo(actorNo.get(i));
	                        showActor.setRoleName(roleName.get(i));

	                        showActorService.saveShowActor(showActor);
	                    }
	                }
	            } else {
	                System.out.println("선택된 배우가 없음");
	            }

	            return "redirect:/admin/showList";
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "redirect:/admin/showInsertForm?error=uploadFailed";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "redirect:/admin/showInsertForm?error=saveFailed";
	        }
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }
	}


	@RequestMapping("/searchActor")
	@ResponseBody
	public List<AdminShowDTO> searchActor(@RequestParam String actorName) {
	    if (actorName == null || actorName.trim().isEmpty()) {
	        return new ArrayList<>();  // 검색어가 없는 경우 빈 리스트 반환
	    }

	    List<Actor> actors = adminActorService.findAllActor(Sort.by(Sort.Direction.DESC, "actorNo"));
	    return actors.stream()
	                 .filter(actor -> actor.getActorName().contains(actorName))
	                 .map(actor -> new AdminShowDTO(actor.getActorNo(), actor.getActorName(), actor.getShowActorFile() != null ? actor.getShowActorFile().getFileNo() : null))
	                 .collect(Collectors.toList());
	}


	
	
	
	//-------------------작가 관련
	
	@Autowired
    private AdminActorService adminActorService;
	
	// 작가 리스트
	@GetMapping("actorList")
	public String actorList(@SessionAttribute("login") Optional<Member> optionalMember, Model model, 
	                        @RequestParam(defaultValue = "0") int page,
	                        @RequestParam(defaultValue = "20") int size) {
	    // 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        List<Actor> allActors = adminActorService.findAllActor(Sort.by(Sort.Direction.DESC, "actorNo"));
	        int totalActors = allActors.size();
	        int totalPages = (int) Math.ceil((double) totalActors / size);

	        int start = page * size;
	        int end = Math.min(start + size, totalActors);

	        List<Actor> actors = allActors.subList(start, end);
	        
	        model.addAttribute("actors", actors);
	        model.addAttribute("currentPage", page);
	        model.addAttribute("totalPages", totalPages);
	        model.addAttribute("totalActors", totalActors);
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }
	    
	    return "admin/adminActorList";
	}

	
	// 작가 상세보기
	@GetMapping("/actorDetail")
	public String actorDetail(@SessionAttribute("login") Optional<Member> optionalMember, @RequestParam int actorNo, Model model) {
	    // 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        Actor actor = adminActorService.getActorByActorNo(actorNo);
	        model.addAttribute("actor", actor);
	        return "admin/adminActorDetail";
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }
	}

	
	//작가 수정
	@GetMapping("/updateForm")
	public String actorUpdateForm(@RequestParam int actorNo, Model model) {
		model.addAttribute("actor", adminActorService.getActorByActorNo(actorNo));
		
		return "admin/adminActorUpdate";
	}
	
	@PostMapping("/actorUpdate")
	public String actorUpdate(@SessionAttribute("login") Optional<Member> optionalMember, 
	                          @RequestParam(value = "file", required = false) MultipartFile file, 
	                          @ModelAttribute Actor actor) {
	    // 멤버 정보가 존재하는지 확인
	    if (optionalMember.isPresent()) {
	        try {
	            // 기존 Actor 정보 가져오기
	            Actor existingActor = adminActorService.getActorByActorNo(actor.getActorNo());
	            if (existingActor == null) {
	                return "redirect:/admin/actorList?error=actorNotFound";
	            }

	            adminActorService.deleteExistingFile(existingActor);

	            // 파일이 새로 업로드된 경우 파일 처리
	            if (file != null && !file.isEmpty()) {
	                String fileName = saveUploadedFile(file);
	                String fileNo = fileName; // UUID + "_" + originalFileName 사용

	                // 새로운 파일 정보 저장
	                ShowActorFile newShowActorFile = new ShowActorFile();
	                newShowActorFile.setFileNo(fileNo);
	                newShowActorFile.setFilePath(Paths.get("src/main/resources/static/uploads", fileName).toString());
	                newShowActorFile.setFileName(file.getOriginalFilename());
	                newShowActorFile.setFileDate(new Date());

	                // Actor와 파일 관계 설정
	                newShowActorFile.setActor(actor);
	                adminActorService.saveShowActorFile(newShowActorFile); // 새로운 파일 정보 저장
	                
	                // Actor 객체에 새로운 파일 정보 설정
	                actor.setShowActorFile(newShowActorFile);
	                actor.setFileNo(newShowActorFile.getFileNo());
	            } else {
	                // 파일이 업로드되지 않은 경우 기존 파일 정보 유지
	                actor.setShowActorFile(existingActor.getShowActorFile());
	                actor.setFileNo(existingActor.getFileNo());
	            }

	            // 배우 정보 저장
	            adminActorService.saveActor(actor);
	            
	            if (actor.getShowActorFile() != null) {
	                actor.getShowActorFile().setFileNo(actor.getFileNo()); // ShowActorFile의 fileNo 업데이트
	            }
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "redirect:/admin/uploadForm?error=uploadFailed";
	        }
	        
	        return "redirect:/admin/actorDetail?actorNo=" + actor.getActorNo();
	    } else {
	        // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
	        return "redirect:/login";
	    }
	}


	
	//날짜 스트링 저장
	@InitBinder
	public void initBinder1(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, "actorBirth", new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Date.class, "actorDebut", new CustomDateEditor(dateFormat, true));
	}
    
    // 작가 등록 폼
    @GetMapping("/insertForm")
    public String actorInsertForm() {
        return "admin/adminActorInsert"; // JSP 또는 HTML 페이지 이름
    }
    
 // 작가 등록
    @PostMapping("/actorInsert")
    public String actorInsert(@SessionAttribute("login") Optional<Member> optionalMember, 
                              @RequestParam("file") MultipartFile file, 
                              @ModelAttribute Actor actor) {
        // 멤버 정보가 존재하는지 확인
        if (optionalMember.isPresent()) {
            String fileName;
            try {
                fileName = saveUploadedFile(file);
                String fileNo = fileName; // UUID + "_" + originalFileName 사용
                
                ShowActorFile showActorFile = new ShowActorFile();
                showActorFile.setFileNo(fileNo);
                showActorFile.setFilePath(Paths.get("src/main/resources/static/uploads", fileName).toString()); // 경로 수정
                showActorFile.setFileName(file.getOriginalFilename());
                showActorFile.setFileDate(new Date());
                
                // 배우 정보 저장
                adminActorService.saveActor(actor);
                
                // 파일 정보와 배우 관계 설정
                showActorFile.setActor(actor);
                adminActorService.saveShowActorFile(showActorFile);
                
                actor.setShowActorFile(showActorFile);
                actor.setFileNo(showActorFile.getFileNo());
                adminActorService.saveActor(actor);
                
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/admin/insertForm?error=uploadFailed";
            }
            
            return "redirect:/admin/actorList";
        } else {
            // 멤버 정보가 없을 경우의 처리 (예: 로그인 페이지로 리다이렉션)
            return "redirect:/login";
        }
    }



}
