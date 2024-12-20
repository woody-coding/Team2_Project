package com.team2.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "MULTI24_LIKEYO",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"MEMBER_NO", "SHOW_NO"}),
	    @UniqueConstraint(columnNames = {"MEMBER_NO", "ACTOR_NO"})
	})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LikeYO {
	
	@Id
	@Column(name = "LIKE_NO")
	private int likeNO;
	
	@Column(name = "SHOW_NO")
	private int showNO; //fk
	
	@Column(name = "ACTOR_NO")
	private int actorNo; //fk
	
	@Column(name = "MEMBER_NO")
	private int memberNo; //fk
	
	@ManyToOne
	@JoinColumn(name = "SHOW_NO" , insertable = false, updatable = false)
	private Show show;
	
	@ManyToOne
	@JoinColumn(name = "ACTOR_NO" , insertable = false, updatable = false)
	private Actor actor;
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_NO" , insertable = false, updatable = false)
	private Member member;
	
	
}
