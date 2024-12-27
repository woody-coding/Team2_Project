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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "MULTI24_SHOW_ACTOR")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShowActor {
		
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "showactor_seq")
	    @SequenceGenerator(name = "showactor_seq", sequenceName = "MULTI24_SHOWACTOR_SEQ", allocationSize = 1)
		@Column(name = "SHOW_ACTOR", nullable = false)
	    private int ShowActorNo;

	    @Column(name = "SHOW_NO", nullable = false)
		private int showNo;

	    @Column(name = "ACTOR_NO", nullable = false)
	    private int actorNo;

	    @Column(name = "ROLE_NAME", nullable = false)
	    private String roleName;
	    
	    @Column(name = "ACTOR_NAME", length = 30, nullable = false)
		private String actorName;
	    
	    @Column(name = "SHOW_TITLE", length = 1000, nullable = false)
		private String showTitle;

	    @ManyToOne
	    @JoinColumn(name = "SHOW_NO", insertable = false, updatable = false)
	    private Show show;

	    @ManyToOne
	    @JoinColumn(name = "ACTOR_NO", insertable = false, updatable = false)
	    private Actor actor;
	
}
