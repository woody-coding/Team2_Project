package com.team2.project.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.team2.project.model.Inquiry;
import com.team2.project.model.InquiryCategory;
import com.team2.project.model.InquiryFile;
import com.team2.project.model.InquiryStatus;
import com.team2.project.model.Member;
import com.team2.project.repository.InquiryFileRepository;
import com.team2.project.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InquiryService {
	
	private final InquiryRepository inquiryRepository;
	private final InquiryFileRepository inquiryFileRepository;

	@Value("${file.upload-dir}")
	private String fileDir;
	

	   private InquiryFile createAndSaveFile(MultipartFile file, Inquiry inquiry) {
	        try {
	            String originalFilename = file.getOriginalFilename();
	            String storeFileName = createStoreFileName(originalFilename);
	            String fullPath = fileDir + storeFileName;
	            file.transferTo(new File(fullPath));

	            InquiryFile inquiryFile = new InquiryFile();
	            inquiryFile.setInquiry(inquiry);
	            inquiryFile.setInquiryFileName(originalFilename);
	            inquiryFile.setInquiryStoreFileName(storeFileName);
	            inquiryFile.setInquiryFilePath(fullPath);
	            inquiryFile.setInquiryFileDate(LocalDate.now());

	            return inquiryFileRepository.save(inquiryFile);
	        } catch (IOException e) {
	            throw new RuntimeException("파일을 등록하는데 실패했습니다.", e);
	        }
	    }

	    private String createStoreFileName(String originalFilename) {
	        String uuid = UUID.randomUUID().toString();
	        return uuid + originalFilename;
	    }
	    
	    

		// 문의 등록 (이미지 파일 포함)
		@Transactional
		public Inquiry saveInquiry(Member member, InquiryCategory inquiryCategory, String inquiryTitle, String inquiryContent, List<MultipartFile> files) {
			
		    Inquiry inquiry = new Inquiry();
	        inquiry.setMember(member);
	        inquiry.setInquiryDate(LocalDate.now());
		    inquiry.setInquiryCategory(inquiryCategory);
		    inquiry.setInquiryTitle(inquiryTitle);
		    inquiry.setInquiryContent(inquiryContent);

		    // Inquiry 엔티티 먼저 저장
		    Inquiry savedInquiry = inquiryRepository.save(inquiry);

		    // 새 파일 추가
		    if (files != null && !files.isEmpty()) {
		        int currentImageCount = savedInquiry.getInquiryFiles().size();
		        int newImageCount = (int) files.stream().filter(file -> !file.isEmpty()).count();

		        if (currentImageCount + newImageCount > 3) {
		            throw new IllegalArgumentException("최대 3개의 이미지 파일만 업로드 가능합니다.");
		        }

		        for (MultipartFile file : files) {
		            if (!file.isEmpty()) {
		            	createAndSaveFile(file, savedInquiry);
		            }
		        }
		    }

		    // 변경된 Inquiry 엔티티 다시 저장
		    return inquiryRepository.save(savedInquiry);
		}


		
		@Transactional
		public void updateInquiry(
		        Integer inquiryNo, 
		        InquiryCategory inquiryCategory,
		        String inquiryTitle,
		        String inquiryContent, 
		        List<MultipartFile> newFiles, 
		        List<String> existingFileIds) {
		    
		    Inquiry inquiry = inquiryRepository.findById(inquiryNo)
		        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 문의 번호 입니다."));

		    // 문의 내용 업데이트
		    inquiry.setInquiryCategory(inquiryCategory);
		    inquiry.setInquiryTitle(inquiryTitle);
		    inquiry.setInquiryContent(inquiryContent);

		    // 기존 파일 삭제
		    List<InquiryFile> existingFiles = inquiry.getInquiryFiles();
		    
		    for (InquiryFile file : existingFiles) {
		        System.out.println("삭제 하려면 여기에 들어와야함! 개수 맞는지 확인하자!!");
		        
		        // existingFileIds가 null일 경우 해당 파일 삭제
		        if (existingFileIds == null) {
		            inquiryFileRepository.deleteById(file.getInquiryFileNo());
		            continue; // 다음 파일로 넘어감
		        }

		        // existingFileIds에 포함되지 않은 파일 삭제
		        if (!existingFileIds.contains(file.getInquiryFileNo())) {
		            inquiryFileRepository.deleteById(file.getInquiryFileNo());
		        }
		    }

		    // 새 파일 추가
		    if (newFiles != null && !newFiles.isEmpty()) {
		        for (MultipartFile file : newFiles) {
		            if (!file.isEmpty()) {
		                System.out.println("새롭게 파일 추가 하려면 여기에 들어와야함! 개수 맞는지 확인하자!!");
		                InquiryFile newFile = createAndSaveFile(file, inquiry);
		                inquiry.getInquiryFiles().add(newFile); // 새로운 파일 추가
		                System.out.println("여기까지 왓으면 inquiry 객체에도 해당 file이 저장됨!! 리스트의 한 요소로 !@@@@@@@@@@@@");
		            }
		        }
		    }

		    inquiryRepository.save(inquiry); // 변경된 Inquiry 객체 저장
		}


	
	// 전체 문의 내역 조회 + 이미지 포함 (날짜로 검색 x)
	public List<Inquiry> findInquiry(int memberNo) {
		return inquiryRepository.findByMember_MemberNo(memberNo, Sort.by(Sort.Direction.DESC, "inquiryNo"));
	}
	
	// 전체 문의 내역 조회 + 이미지 포함 (날짜로 검색 o)
	public List<Inquiry> findInquiryByDate(int memberNo, LocalDate startDate, LocalDate endDate) {
		return inquiryRepository.findAllByDateRange(memberNo, startDate, endDate);
	}
	
	// 해당 문의 삭제
	@Transactional
	public void deleteInquiry(int memberNo, int inquiryNo) {
	    Inquiry inquiry = inquiryRepository.findById(inquiryNo)
	            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 문의 번호 입니다."));
	        
	    // 현재 로그인한 사용자가 해당 문의의 작성자인지 확인
	    if (inquiry.getMember().getMemberNo() != memberNo) {
	        throw new IllegalArgumentException("회원이 일치하지 않습니다.");
	    }
	    
	    // 관련된 InquiryFile 삭제
	    List<InquiryFile> inquiryFiles = inquiry.getInquiryFiles();
	    for (InquiryFile inquiryFile : inquiryFiles) {
	        inquiryFileRepository.delete(inquiryFile); // DB에서 삭제
	    }
	    
	    // Inquiry 삭제
	    inquiryRepository.delete(inquiry);
	}
	
	// 문의 고유번호로 해당 문의 전체 내용 반환
	public Inquiry findById(int inquiryNo) {
	    return inquiryRepository.findById(inquiryNo)
	        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 문의 번호 입니다 : " + inquiryNo));
	}

	
	public List<Inquiry> findInquiryByStatus(int memberNo, InquiryStatus status) {
		return inquiryRepository.findByMember_MemberNoAndInquiryStatus(memberNo, status, Sort.by(Sort.Direction.DESC, "inquiryNo"));
	}
	
}