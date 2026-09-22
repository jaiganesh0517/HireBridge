package com.jai.HireBridge.dto;

import java.time.LocalDateTime;

import com.jai.HireBridge.model.AppliStatus;

public class ApplicantsResponse 
{
  
	private String userName;
	private String about;
	private String branch;
	private double cgpa;
	private String skills;
	private String emailId;
	private Long applicationId;
	private AppliStatus status;
	private LocalDateTime appliedAt;
	private Long studentId;
	
	public ApplicantsResponse(String userName, String about, String branch, double cgpa, String skills, String emailId,
			Long applicationId, AppliStatus status, LocalDateTime appliedAt, Long studentId) {
		super();
		this.userName = userName;
		this.about = about;
		this.branch = branch;
		this.cgpa = cgpa;
		this.skills = skills;
		this.emailId = emailId;
		this.applicationId = applicationId;
		this.status = status;
		this.appliedAt = appliedAt;
		this.studentId = studentId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public double getCgpa() {
		return cgpa;
	}
	public void setCgpa(double cgpa) {
		this.cgpa = cgpa;
	}
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	public Long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
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
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	
	
}
