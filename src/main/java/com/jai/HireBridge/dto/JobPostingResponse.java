package com.jai.HireBridge.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jai.HireBridge.model.Status;

public class JobPostingResponse {

	private Long jobPostId;
	private String title;
	private String descrip;
	private BigDecimal ctc;
	private double minCgpa;
	private LocalDateTime deadline;
	private Status jobStatus;
	
	
	public JobPostingResponse(Long jobPostId, String title, String descrip, BigDecimal ctc, double minCgpa,
			LocalDateTime deadline, Status jobStatus) {
		super();
		this.jobPostId = jobPostId;
		this.title = title;
		this.descrip = descrip;
		this.ctc = ctc;
		this.minCgpa = minCgpa;
		this.deadline = deadline;
		this.jobStatus = jobStatus;
	}
	public Long getJobPostId() {
		return jobPostId;
	}
	public void setJobPostId(Long jobPostId) {
		this.jobPostId = jobPostId;
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
	public LocalDateTime getDeadline() {
		return deadline;
	}
	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}
	public Status getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(Status jobStatus) {
		this.jobStatus = jobStatus;
	}
	
	
	
}
