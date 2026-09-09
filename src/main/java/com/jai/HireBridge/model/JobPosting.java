package com.jai.HireBridge.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobPosting 
{
  @Id
  @GeneratedValue
  private Long jobPostId;
  
  private Long recruiterId;
  
  @NotNull
  private String title;
  
  private String descrip;
  
  private BigDecimal ctc;
  
  private double minCgpa;
  @Enumerated(EnumType.STRING)
  private Status jobStatus;
  
  @NotNull
  private LocalDateTime deadline;

  
  public JobPosting() {
	super();
}

  public JobPosting(Long jobPostId, Long recruiterId, @NotNull String title, String descrip, BigDecimal ctc,
		double minCgpa, Status jobStatus, @NotNull LocalDateTime deadline) {
	super();
	this.jobPostId = jobPostId;
	this.recruiterId = recruiterId;
	this.title = title;
	this.descrip = descrip;
	this.ctc = ctc;
	this.minCgpa = minCgpa;
	this.jobStatus = jobStatus;
	this.deadline = deadline;
  }

  public Long getJobPostId() {
	return jobPostId;
  }

  public void setJobPostId(Long jobPostId) {
	this.jobPostId = jobPostId;
  }

  public Long getRecruiterId() {
	return recruiterId;
  }

  public void setRecruiterId(Long recruiterId) {
	this.recruiterId = recruiterId;
  }

  public String getTitle() {
	return title;
  }

  public void setTitle(String title) {
	this.title = title;
  }

  public String getDescrip() {
	return descrip;
  }

  public void setDescrip(String descrip) {
	this.descrip = descrip;
  }

  public BigDecimal getCtc() {
	return ctc;
  }

  public void setCtc(BigDecimal ctc) {
	this.ctc = ctc;
  }

  public double getMinCgpa() {
	return minCgpa;
  }

  public void setMinCgpa(double minCgpa) {
	this.minCgpa = minCgpa;
  }

  public Status getJobStatus() {
	return jobStatus;
  }

  public void setJobStatus(Status jobStatus) {
	this.jobStatus = jobStatus;
  }

  public LocalDateTime getDeadline() {
	return deadline;
  }

  public void setDeadline(LocalDateTime deadline) {
	this.deadline = deadline;
  }

  @Override
  public String toString() {
	return "JobPosting [jobPostId=" + jobPostId + ", recruiterId=" + recruiterId + ", title=" + title + ", descrip="
			+ descrip + ", ctc=" + ctc + ", minCgpa=" + minCgpa + ", jobStatus=" + jobStatus + ", deadline=" + deadline
			+ "]";
  }
  
  
  
}
