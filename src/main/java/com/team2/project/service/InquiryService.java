package com.team2.project.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
import com.team2.project.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InquiryService {
	
	private final InquiryRepository inquiryRepository;

	@Value("${file.upload-dir}")
	private String fileDir;
	
	// 원본 파일명에서 확장자 뽑아내는 메서드
	public String extractExt(String originalFilename) {
		int position = originalFilename.lastIndexOf(".");
		return originalFilename.substring(position + 1);
	}
	
	// 문의 등록 및 수정 (이미지 포함)
	@Transactional
	public Inquiry saveInquiry(Member member, InquiryCategory inquiryCategory, String inquiryTitle, String inquiryContent, List<MultipartFile> files, Integer inquiryNo) {
	    Inquiry inquiry;

	    // inquiryNo가 null이 아니면 기존 문의를 조회
	    if (inquiryNo != null) {
	        inquiry = inquiryRepository.findById(inquiryNo)
	            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 문의 번호 입니다."));
	    } else {
	        inquiry = new Inquiry();
	        inquiry.setMember(member);
	        inquiry.setInquiryDate(LocalDate.now());
	    }

	    // 공통 필드 업데이트
	    inquiry.setInquiryCategory(inquiryCategory);
	    inquiry.setInquiryTitle(inquiryTitle);
	    inquiry.setInquiryContent(inquiryContent);

	    // 이미지 파일 처리 및 저장
	    if (files != null && !files.isEmpty()) {
	        // 현재 이미지 개수
	        int currentImageCount = inquiry.getInquiryFiles().size();
	        int newImageCount = 0;

	        // 새로 추가할 이미지 개수 확인
	        for (MultipartFile file : files) {
	            if (!file.isEmpty()) {
	                newImageCount++;
	            }
	        }

	        // 총 이미지 개수가 3개를 초과하는지 확인
	        if (currentImageCount + newImageCount > 3) {
	            throw new IllegalArgumentException("최대 3개의 이미지 파일만 업로드 가능합니다.");
	        }

	        // 기존 이미지는 삭제하지 않고 새로운 이미지만 추가
	        for (MultipartFile file : files) {
	            if (!file.isEmpty()) {
	                InquiryFile inquiryFile = new InquiryFile();
	                inquiryFile.setInquiry(inquiry);
	                inquiryFile.setInquiryFileName(file.getOriginalFilename());
	                inquiryFile.setInquiryFileDate(LocalDate.now());

	                // 파일 경로 설정
	                String ext = extractExt(file.getOriginalFilename());
	                String fileFullPath = fileDir + inquiryFile.getInquiryFileNo() + "." + ext;
	                inquiryFile.setInquiryFilePath(fileFullPath);
	                inquiryFile.setInquiryStoreFileName(inquiryFile.getInquiryFileNo() + "." + ext);

	                // 로컬에 파일 저장 + 디비에 파일 경로 저장
	                try {
	                    File destinationFile = new File(fileFullPath);
	                    file.transferTo(destinationFile); // 실제 파일 저장
	                    inquiry.getInquiryFiles().add(inquiryFile); // 새로운 이미지 추가
	                } catch (IOException e) {
	                    throw new RuntimeException("파일 업로드 중 오류 발생: " + e.getMessage());
	                }
	            }
	        }
	    }

	    return inquiryRepository.save(inquiry);
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
	        
	        // inquiryFiles 리스트에서 InquiryFile 객체를 제거하여 고아 객체로 처리
	        inquiry.getInquiryFiles().forEach(inquiryFile -> {
	            inquiryFile.setInquiry(null); // 부모 참조 제거
	        });
	        
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
	
	// 관리자 답변 생성
	@Transactional
	public void updateInquiryByAdmin(int inquiryNo, String inquiryAnswer) {
		inquiryRepository.updateByAdmin(inquiryNo, inquiryAnswer);
	}
}