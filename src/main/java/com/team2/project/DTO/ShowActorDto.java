package com.team2.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowActorDto {
	private int showNo;
	private int actorNo;
	private String roleName;
	private String actorName;
	private String showTitle;
	private String filePath;

}
