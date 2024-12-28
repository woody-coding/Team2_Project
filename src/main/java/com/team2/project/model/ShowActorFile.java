package com.team2.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "MULTI24_SHOWACTOR_FILE")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ShowActorFile {

    @Id
    @Column(name = "FILE_NO", nullable = false, unique = true)
    private String fileNo; // UUID를 문자열로 저장

    @Column(name = "FILE_PATH", nullable = false, length = 255)
    private String filePath;

    @Column(name = "FILE_NAME", nullable = false, length = 255)
    private String fileName;

    @Column(name = "FILE_DATE", nullable = false)
    private Date fileDate;

    @Column(name = "SHOW_NO")
    private Integer showNo; 

    @Column(name = "ACTOR_NO")
    private Integer actorNo;
    
    @ManyToOne
    @JoinColumn(name = "SHOW_NO", insertable = false, updatable = false)
    private Show show;
    
    @ManyToOne
    @JoinColumn(name = "ACTOR_NO", insertable = false, updatable = false)
    private Actor actor;

    public ShowActorFile() {
        this.fileNo = UUID.randomUUID().toString(); // UUID 생성
    }	
}
