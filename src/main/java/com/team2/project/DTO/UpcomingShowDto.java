package com.team2.project.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpcomingShowDto {
	private int showNo;
	private Date openDate;
	private String openDateString;
	private String showTitle;
	private String showInfo;
	private String filePath;
	private int dayDiff;
}
