package com.team2.project.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team2.project.model.Seat;
import com.team2.project.model.Show;
import com.team2.project.model.ShowActorFile;
import com.team2.project.model.id.SeatId;
import com.team2.project.repository.AdminShowRepository;
import com.team2.project.repository.SeatRepository;
import com.team2.project.repository.ShowActorFileRepository;

@Service
public class AdminShowService {

	@Autowired
	AdminShowRepository adminShowRepository;
	
	@Autowired
	ShowActorFileRepository showActorFileRepository;
	
	@Autowired
	private SeatRepository seatRepository;
	
	@Transactional
	public void createSeatsForShow(Show show) {
	    LocalDate startDate = show.getStartDate().toInstant()
	                                .atZone(ZoneId.systemDefault())
	                                .toLocalDate();
	    LocalDate endDate = show.getEndDate().toInstant()
	                            .atZone(ZoneId.systemDefault())
	                            .toLocalDate();

	    List<Seat> seats = new ArrayList<>();
	    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
	        for (int seatNo = 1; seatNo <= 45; seatNo++) {
	            // 복합 키 객체 생성
	            SeatId seatId = new SeatId(seatNo, show.getShowNo(), java.sql.Date.valueOf(date));
	            
	            // 중복 체크
	            if (!seatRepository.existsById(seatId)) {
	                Seat seat = new Seat();
	                seat.setSeatNo(seatNo); // 각 필드 개별 설정
	                seat.setShowNo(show.getShowNo());
	                seat.setShowDate(java.sql.Date.valueOf(date));
	                
	             // 자리 공간 설정: 1~9는 A, 10~18은 B, 19~27은 C, 28~36은 D, 37~45는 E
	                char section = (char) ('A' + (seatNo - 1) / 9); // 9자리마다 알파벳 변경
	                String seatNumber = String.format("%02d", (seatNo - 1) % 9 + 1); // 두 자리 형식으로 변환
	                seat.setSeatSpace(section + seatNumber); // 01부터 시작
	                
	                seat.setIsBook("N"); // 기본값: 예약되지 않음
	                seat.setTotSeat(45);
	                seat.setLeftSeat(45);
	                seat.setShowTime(show.getShowStartTime());
	                seats.add(seat);
	            } else {
	                System.out.println("중복된 좌석: " + seatNo + ", 날짜: " + date);
	            }
	        }
	    }
	    // 일괄 저장
	    if (!seats.isEmpty()) {
	        seatRepository.saveAll(seats);
	    } else {
	        System.out.println("저장할 좌석이 없습니다.");
	    }
	}



	
	
	public List<Show> findAllShow(Sort sort){
		return adminShowRepository.findAll(sort);
	}
	
	public Show getShowByShowNo(int showNo) {
        return adminShowRepository.findByShowNo(showNo);
	}
	@Transactional
    public ShowActorFile saveShowActorFile(ShowActorFile showActorFile) {
        return showActorFileRepository.save(showActorFile);
    }

	public Show saveShow(Show show) {
		return adminShowRepository.save(show);
	}
	
	public ShowActorFile getShowActorFileByShowNo(int showNo) {
		Show show = getShowByShowNo(showNo);
		if (show != null) {
			return showActorFileRepository.findByShow(show);
		}
		return null; // 공연이 존재하지 않는 경우 null 반환
	}
	
	@Transactional
	public void deleteShowActorFile(ShowActorFile showActorFile) {
	    showActorFileRepository.delete(showActorFile);
	}
	
	public void deleteExistingFile(Show existingShow) {
	    String fileNo = existingShow.getShowActorFile().getFileNo(); // fileNo가 String 타입
	    ShowActorFile existingFile = showActorFileRepository.findById(fileNo).orElse(null);
	    if (existingFile != null) {
	        Path existingFilePath = Paths.get(existingFile.getFilePath());
	        try {
	            Files.deleteIfExists(existingFilePath); // 로컬 파일 시스템에서 삭제
	            deleteShowActorFile(existingFile); // DB에서 삭제
	        } catch (IOException e) {
	            e.printStackTrace(); // 예외 처리
	        }
	    }
	}
}
