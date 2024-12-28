package com.team2.project.model;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(cascade = CascadeType.PERSIST) // 또는 CascadeType.ALL
    @JoinColumn(name = "SHOW_NO", referencedColumnName = "SHOW_NO", nullable = false)
    private Show show;

    @ManyToOne(cascade = CascadeType.PERSIST) // 또는 CascadeType.ALL
    @JoinColumn(name = "ACTOR_NO", referencedColumnName = "ACTOR_NO", nullable = false)
    private Actor actor;


    public ShowActorFile() {
        this.fileNo = UUID.randomUUID().toString(); // UUID 생성
        this.fileDate = new Date();
    }
}
