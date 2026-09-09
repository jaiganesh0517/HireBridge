package com.jai.HireBridge.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames= {"studentId" ,"jobId"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application
{
  @Id
  @GeneratedValue
  private Long applicationId;
  
  private Long studentId;
  
  private Long jobId;
  
  @Enumerated(EnumType.STRING)
  private AppliStatus status;
  
  private LocalDateTime appliedAt;

  
  public Application() {
	super();
}

  public Application(Long applicationId, Long studentId, Long jobId, AppliStatus status, LocalDateTime appliedAt) {
	super();
	this.applicationId = applicationId;
	this.studentId = studentId;
	this.jobId = jobId;
	this.status = status;
	this.appliedAt = appliedAt;
  }

  public Long getApplicationId() {
	return applicationId;
  }

  public void setApplicationId(Long applicationId) {
	this.applicationId = applicationId;
  }

  public Long getStudentId() {
	return studentId;
  }

  public void setStudentId(Long studentId) {
	this.studentId = studentId;
  }

  public Long getJobId() {
	return jobId;
  }

  public void setJobId(Long jobId) {
	this.jobId = jobId;
  }

  public AppliStatus getStatus() {
	return status;
  }

  public void setStatus(AppliStatus status) {
	this.status = status;
  }

  public LocalDateTime getAppliedAt() {
	return appliedAt;
  }

  public void setAppliedAt(LocalDateTime appliedAt) {
	this.appliedAt = appliedAt;
  }

  @Override
  public String toString() {
	return "Application [applicationId=" + applicationId + ", studentId=" + studentId + ", jobId=" + jobId + ", status="
			+ status + ", appliedAt=" + appliedAt + "]";
  }
	
  
	
}
