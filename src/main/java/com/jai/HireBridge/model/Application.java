package com.jai.HireBridge.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames= {"studentId" ,"jobId"}))
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

  private String resumeKey;
  private String resumeFileName;
  private String resumeContentType;
  private Long resumeSize;
  private LocalDateTime resumeUploadedAt;
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
  

  public String getResumeKey() {
	return resumeKey;
} 

  public void setResumeKey(String resumeKey) {
	this.resumeKey = resumeKey;
  }

  public String getResumeFileName() {
	return resumeFileName;
  }

  public void setResumeFileName(String resumeFileName) {
	this.resumeFileName = resumeFileName;
  }

  public String getResumeContentType() {
	return resumeContentType;
  }

  public void setResumeContentType(String resumeContentType) {
	this.resumeContentType = resumeContentType;
  }

  public Long getResumeSize() {
	return resumeSize;
  }

  public void setResumeSize(Long resumeSize) {
	this.resumeSize = resumeSize;
  }

  public LocalDateTime getResumeUploadedAt() {
	return resumeUploadedAt;
  }

  public void setResumeUploadedAt(LocalDateTime resumeUploadedAt) {
	this.resumeUploadedAt = resumeUploadedAt;
  }

  @Override
  public String toString() {
	return "Application [applicationId=" + applicationId + ", studentId=" + studentId + ", jobId=" + jobId + ", status="
			+ status + ", appliedAt=" + appliedAt + "]";
  }
	
  
	
}
