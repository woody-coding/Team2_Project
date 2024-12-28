package com.team2.project.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@Table(name = "MULTI24_INQUIRY_FILE")
public class InquiryFile {

	@Id
	@Column(name = "INQUIRY_FILE_NO")
	private String inquiryFileNo;
	
	@ManyToOne
	@JoinColumn(name = "INQUIRY_NO", insertable = false, updatable = false)
	private Inquiry inquiry;
	
	@Column(name = "INQUIRY_FILE_PATH", nullable = false, length = 255)
	private String inquiryFilePath;
	
	@Column(name = "INQUIRY_FILE_NAME", nullable = false, length = 255)
	private String inquiryFileName;
	
	@Column(name = "INQUIRY_FILE_DATE", nullable = false)
	private LocalDate inquiryFileDate;
	
	public InquiryFile() {
		this.inquiryFileNo = UUID.randomUUID().toString();
		this.inquiryFileDate = LocalDate.now();
	}
	
}