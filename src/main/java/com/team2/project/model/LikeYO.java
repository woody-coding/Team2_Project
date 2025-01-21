package com.team2.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "MULTI24_LIKEYO",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"MEMBER_NO", "ACTOR_NO"})
	})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LikeYO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "likeYo_seq")
    @SequenceGenerator(name = "likeYo_seq", sequenceName = "MULTI24_LIKEYO_SEQ", allocationSize = 1)
	@Column(name = "LIKE_NO")
	private int likeNO;
	
	@Column(name = "ACTOR_NO")
	private Integer actorNo; //fk
	
	@Column(name = "MEMBER_NO")
	private int memberNo; //fk
	
	@Column(name = "STATUS", nullable = false)
	private String status;
	
	@ManyToOne
	@JoinColumn(name = "ACTOR_NO" , insertable = false, updatable = false)
	private Actor actor;
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_NO" , insertable = false, updatable = false)
	private Member member;
	
	
}
