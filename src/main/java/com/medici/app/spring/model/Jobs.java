package com.medici.app.spring.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A Jobs.
 */
@Entity
@Table(name = "jobs")
public class Jobs implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "job_id")
	private Long jobId;

	@Column(name = "job_name")
	private String jobName;

	@Column(name = "regular_expression")
	private String regularExpression;

	@Column(name = "jhi_enable")
	private Integer enable;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJobId() {
		return jobId;
	}

	public Jobs jobId(Long jobId) {
		this.jobId = jobId;
		return this;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public Jobs jobName(String jobName) {
		this.jobName = jobName;
		return this;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getRegularExpression() {
		return regularExpression;
	}

	public Jobs regularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
		return this;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	public Integer getEnable() {
		return enable;
	}

	public Jobs enable(Integer enable) {
		this.enable = enable;
		return this;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
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
		Jobs jobs = (Jobs) o;
		if (jobs.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), jobs.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Jobs{" + "id=" + getId() + ", jobId=" + getJobId() + ", jobName='" + getJobName() + "'" + ", regularExpression='" + getRegularExpression() + "'" + ", enable=" + getEnable() + "}";
	}
}
