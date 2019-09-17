package com.medici.app.spring.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author a73s
 *
 */
@Entity
@Table(name = "ACTOR")
public class Actor implements Serializable {

	private static final long serialVersionUID = 8628206533884541242L;

	@Id
	@Column(name = "ACTOR_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer actor_id;

	@Column(name = "FIRST_NAME")
	private String first_name;

	@Column(name = "LAST_NAME")
	private String last_name;

	@Column(name = "LAST_UPDATE")
	private Date last_update;

	public Actor() {
		super();
	}

	public Integer getActor_id() {
		return actor_id;
	}

	public void setActor_id(Integer actor_id) {
		this.actor_id = actor_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public Date getLast_update() {
		return last_update;
	}

	public void setLast_update(Date last_update) {
		this.last_update = last_update;
	}

	@Override
	public String toString() {
		return "Actor [actor_id=" + actor_id + ", first_name=" + first_name + ", last_name=" + last_name + ", last_update=" + last_update + "]";
	}

}
