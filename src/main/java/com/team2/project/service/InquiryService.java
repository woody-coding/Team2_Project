package com.team2.project.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.team2.project.model.Inquiry;
import com.team2.project.model.InquiryCategory;
import com.team2.project.model.InquiryFile;
import com.team2.project.model.Member;
import com.team2.project.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InquiryService {
	
	private final InquiryRepository inquiryRepository;

	@Value("${file.dir}")
	private String fileDir;
	
	// 원본 파일명에서 확장자 뽑아내는 메서드
	public String extractExt(String originalFilename) {
		int position = originalFilename.lastIndexOf(".");
		return originalFilename.substring(position + 1);
	}
	
	// 문의 등록 및 업데이트 (이미지 포함)
	@Transactional
	public Inquiry saveInquiry(Member member, InquiryCategory inquiryCategory, String inquiryTitle, String inquiryContent, List<MultipartFile> files) {
		
		Inquiry inquiry = new Inquiry();
		List<InquiryFile> inquiryFiles = new ArrayList<>();
		inquiry.setMember(member);
		inquiry.setInquiryCategory(inquiryCategory);
		inquiry.setInquiryTitle(inquiryTitle);
		inquiry.setInquiryContent(inquiryContent);
		
		
        // 이미지 파일 처리 및 저장
        if (files != null && files.size() <= 3) { // 최대 3개 파일 제한
            for (MultipartFile file : files) {
                InquiryFile inquiryFile = new InquiryFile();
                inquiryFile.setInquiry(inquiry);
                inquiryFile.setInquiryFileName(file.getOriginalFilename());
                inquiryFile.setInquiryFileDate(LocalDate.now());
                
                // 파일 경로 설정
                String ext = extractExt(file.getOriginalFilename());
                String fileFullPath = fileDir + inquiryFile.getInquiryFileNo() + "." + ext;
                inquiryFile.setInquiryFilePath(fileFullPath);

                // 로컬에 파일 저장 + 디비에 파일 경로 저장
                try {
                	inquiryFiles.add(inquiryFile);
                    File destinationFile = new File(fileFullPath);
                    file.transferTo(destinationFile); // 실제 파일 저장
                } catch (IOException e) {
                    throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage());
                }
            }
            
            inquiry.setInquiryFiles(inquiryFiles);
            
            return inquiryRepository.save(inquiry);
            
        } else {
            throw new IllegalArgumentException("최대 3개의 파일만 업로드 가능합니다.");
        }
	}
	
	// 전체 문의 내역 조회 + 이미지 포함 (날짜로 검색 x)
	public List<Inquiry> findInquiry(int memberNo) {
		return inquiryRepository.findByMember_MemberNo(memberNo, Sort.by(Sort.Direction.DESC, "inquiryDate"));
	}
	
	// 전체 문의 내역 조회 + 이미지 포함 (날짜로 검색 o)
	public List<Inquiry> findInquiryByDate(int memberNo, LocalDate startDate, LocalDate endDate) {
		return inquiryRepository.findAllByDateRange(memberNo, startDate, endDate);
	}
	
	// 해당 문의 삭제
	public void deleteInquiry(int memberNo, int inquiryNo) {
		inquiryRepository.deleteByMemberAndInquiry(memberNo, inquiryNo);
	}
	
	// 문의 고유번호로 해당 문의 전체 내용 반환
	public Optional<Inquiry> findById(int inquiryNo) {
		return inquiryRepository.findById(inquiryNo);
	}
	
	// 관리자 답변 생성
	public void updateInquiryByAdmin(int inquiryNo, String inquiryAnswer) {
		inquiryRepository.updateByAdmin(inquiryNo, inquiryAnswer);
	}
}