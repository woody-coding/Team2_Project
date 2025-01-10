package com.team2.project.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.team2.project.DTO.ShowFileDto;
import com.team2.project.DTO.UpcomingShowDto;
import com.team2.project.model.Seat;
import com.team2.project.model.Show;
import com.team2.project.model.ShowActorFile;
import com.team2.project.repository.FileRepo;
import com.team2.project.repository.SeatRepo;
import com.team2.project.repository.ShowRepo;

@Service
public class ShowMainService {
	
	@Autowired
	private ShowRepo showRepo;
	
	@Autowired
	private FileRepo fileRepo;

	@Autowired
	private SeatRepo seatRepo;
	
	Date today = new Date();
	
	public void goShowMain(int memberNo, Model model) {
		//이번주 인기작 구하기 (남은 좌석수)
		
		List<Show> showList = showRepo.findTop7ByOrderByLikesDesc();
		List<ShowFileDto> list = new ArrayList<>();
		
		for(Show tmp : showList) {
			ShowFileDto dto = new ShowFileDto();
			dto.setShowNo(tmp.getShowNo());
			
			ShowActorFile showFile = fileRepo.findByShowNo(tmp.getShowNo());
			if(showFile != null) {
				String path = showFile.getFilePath() + showFile.getFileName();
				dto.setFilePath(path);
			}else {
				dto.setFilePath(null);
			}
			
			list.add(dto);			
		}
		
		//오픈임박 공연 구하기	
		List<Show> upcoming = showRepo.findByOpenDateGreaterThanEqualOrderByOpenDateAsc(today);
		List<UpcomingShowDto> upcomingList = new ArrayList<>();
		
		for(Show tmp : upcoming) {
			UpcomingShowDto dto = new UpcomingShowDto();
			dto.setShowNo(tmp.getShowNo());
			dto.setOpenDate(tmp.getOpenDate());
			
			//OpenDate를 string으로 변환해서 담음
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
			String formattedDate = sdf.format(tmp.getOpenDate());
			dto.setOpenDateString(formattedDate);
			
			dto.setShowTitle(tmp.getShowTitle());
			dto.setShowInfo(tmp.getShowInfo());

			// 파일경로 설정
			ShowActorFile showFile = fileRepo.findByShowNo(tmp.getShowNo());
			if(showFile != null) {
				String path = showFile.getFilePath() + showFile.getFileName();
				dto.setFilePath(path);
			} else {
				dto.setFilePath(null);
			}
			
			// D-day 계산
			long timeDiff = tmp.getOpenDate().getTime() - today.getTime();
			int dayDiff = (int)(timeDiff / (1000 * 3600 * 24));
			if(dayDiff > 0) {
				dayDiff += 1;
			}
			dto.setDayDiff(dayDiff);
			
			upcomingList.add(dto);
		}

		model.addAttribute("memberNo", memberNo);
		model.addAttribute("list", list);
		model.addAttribute("upcoming", upcomingList);
	}
	
	public void bookSeat(int showNo, int memberNo, String date, Model model) {
		Show thisShow = showRepo.findByShowNo(showNo);
		
		model.addAttribute("memberNo", memberNo);
		model.addAttribute("show", thisShow);
		model.addAttribute("showDate", date);
	}

	public List<Seat> allSeatList(String showNo, String showDate) {
		try{
			int showNoInt = Integer.parseInt(showNo);
			
			SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
			Date bookDate = sdt.parse(showDate);
			
			List<Seat> allSeatList = seatRepo.findByShowNoAndShowDate(showNoInt, bookDate);
			return allSeatList;
			
		} catch (NumberFormatException e) {
	        // seatNo가 숫자로 변환할 수 없는 경우
			System.out.println("seatNo 숫자 변환 불가능");
	        return null;
	    } catch (Exception e) {
	        // bookDate가 날짜 형식에 맞지 않는 경우
	    	System.out.println("bookDate 날짜 형식 오류");
	        return null;
	    }
	}

}
