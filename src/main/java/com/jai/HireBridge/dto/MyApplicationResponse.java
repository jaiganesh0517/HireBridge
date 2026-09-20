package com.jai.HireBridge.dto;

import java.time.LocalDateTime;

import com.jai.HireBridge.model.AppliStatus;
import com.jai.HireBridge.model.Status;

public class MyApplicationResponse 
{
  
	private Long applicationId;
	private Long jobId;
	private AppliStatus status;
	private LocalDateTime appliedAt;
	private String title;
	private String discrip;
	private Status jobStatus;
	
	
	public MyApplicationResponse(Long applicationId, Long jobId, AppliStatus status, LocalDateTime appliedAt,
			String title, String discrip, Status jobStatus) {
		super();
		this.applicationId = applicationId;
		this.jobId = jobId;
		this.status = status;
		this.appliedAt = appliedAt;
		this.title = title;
		this.discrip = discrip;
		this.jobStatus = jobStatus;
	}
	public Long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDiscrip() {
		return discrip;
	}
	public void setDiscrip(String discrip) {
		this.discrip = discrip;
	}
	public Status getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(Status jobStatus) {
		this.jobStatus = jobStatus;
	}
	
	
}
