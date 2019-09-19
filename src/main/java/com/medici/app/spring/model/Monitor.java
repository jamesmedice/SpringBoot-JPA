package com.medici.app.spring.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * A Monitor.
 */
@Entity
@Table(name = "monitor")
public class Monitor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Lob
	@Column(name = "timer")
	private String timer;

	@Column(name = "scheduler")
	private String scheduler;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Monitor name(String name) {
		this.name = name;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimer() {
		return timer;
	}

	public Monitor timer(String timer) {
		this.timer = timer;
		return this;
	}

	public void setTimer(String timer) {
		this.timer = timer;
	}

	public String getScheduler() {
		return scheduler;
	}

	public Monitor scheduler(String scheduler) {
		this.scheduler = scheduler;
		return this;
	}

	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters
	// and setters here, do not remove

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Monitor monitor = (Monitor) o;
		if (monitor.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), monitor.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Monitor{" + "id=" + getId() + ", name='" + getName() + "'" + ", timer='" + getTimer() + "'" + ", scheduler='" + getScheduler() + "'" + "}";
	}
}
