package com.medici.app.spring.sevice.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Jobs entity.
 */
public class JobsDTO implements Serializable {

	private Long id;

	private Long jobId;

	private String jobName;

	private String regularExpression;

	private Integer enable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		JobsDTO jobsDTO = (JobsDTO) o;
		if (jobsDTO.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), jobsDTO.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "JobsDTO{" + "id=" + getId() + ", jobId=" + getJobId() + ", jobName='" + getJobName() + "'" + ", regularExpression='" + getRegularExpression() + "'" + ", enable=" + getEnable() + "}";
	}
}
