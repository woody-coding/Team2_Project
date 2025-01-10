package com.team2.project.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminShowDTO {
	private int actorNo;        // 배우 번호
    private String actorName;   // 배우 이름	

	private String fileNo;
}
