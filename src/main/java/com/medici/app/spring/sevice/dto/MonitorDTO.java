package com.medici.app.spring.sevice.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Lob;

/**
 * A DTO for the Monitor entity.
 */
public class MonitorDTO implements Serializable {

	private Long id;

	private String name;

	@Lob
	private String timer;

	private String scheduler;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimer() {
		return timer;
	}

	public void setTimer(String timer) {
		this.timer = timer;
	}

	public String getScheduler() {
		return scheduler;
	}

	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		MonitorDTO monitorDTO = (MonitorDTO) o;
		if (monitorDTO.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), monitorDTO.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "MonitorDTO{" + "id=" + getId() + ", name='" + getName() + "'" + ", timer='" + getTimer() + "'" + ", scheduler='" + getScheduler() + "'" + "}";
	}
}
