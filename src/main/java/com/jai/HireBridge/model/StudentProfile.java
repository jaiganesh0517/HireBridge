package com.jai.HireBridge.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;


@Entity

public class StudentProfile 
{
	@Id
	@Column(name = "user_id")
	private Long userId;

	@OneToOne
	@MapsId
	@JoinColumn(name = "user_id")
	private Users user;
	
    private String about;
    private String branch;
    private int batchYear;
    private double cgpa;
    private String skills;
    private String resumeKey;
    private String resumeFileName;
    private String resumeContentType;
    private Long resumeSize;
    private LocalDateTime resumeUploadedAt;
    
	public StudentProfile() {
		super();
	}
	
	public StudentProfile(Users user, String about, String branch, int batchYear, double cgpa,
			String skills, String resumeKey, String resumeFileName, String resumeContentType, Long resumeSize,
			LocalDateTime resumeUploadedAt) {
		super();
		this.user = user;
		this.about = about;
		this.branch = branch;
		this.batchYear = batchYear;
		this.cgpa = cgpa;
		this.skills = skills;
		this.resumeKey = resumeKey;
		this.resumeFileName = resumeFileName;
		this.resumeContentType = resumeContentType;
		this.resumeSize = resumeSize;
		this.resumeUploadedAt = resumeUploadedAt;
	}
	public Users getUser() {
		return user;
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
	public int getBatchYear() {
		return batchYear;
	}
	public void setBatchYear(int batchYear) {
		this.batchYear = batchYear;
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
		return "StudentProfile [userId=" + userId + ", user=" + user + ", about=" + about + ", branch=" + branch
				+ ", batchYear=" + batchYear + ", cgpa=" + cgpa + ", skills=" + skills + ", resumeKey=" + resumeKey
				+ ", resumeFileName=" + resumeFileName + ", resumeContentType=" + resumeContentType + ", resumeSize="
				+ resumeSize + ", resumeUploadedAt=" + resumeUploadedAt + "]";
	}
    
    
}
