package com.jai.HireBridge.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "job_id"}))
public class SavedJob 
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long jobSavedId;
	
	@Column(nullable = false)
	private Long studentId;
	
	@Column(nullable = false)
	private Long jobId;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime savedAt;

	
	public SavedJob() {
		super();
	}


	public SavedJob( Long studentId, Long jobId) {
		super();
		this.studentId = studentId;
		this.jobId = jobId;
	}


	public Long getJobSavedId() {
		return jobSavedId;
	}


	public void setJobSavedId(Long jobSavedId) {
		this.jobSavedId = jobSavedId;
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


	public LocalDateTime getSavedAt() {
		return savedAt;
	}


	public void setSavedAt(LocalDateTime savedAt) {
		this.savedAt = savedAt;
	}


	@PrePersist
	void onCreate() {
		this.savedAt = LocalDateTime.now();
	}
	@Override
	public String toString() {
		return "SavedJob [jobSavedId=" + jobSavedId + ", studentId=" + studentId + ", jobId=" + jobId + ", savedAt="
				+ savedAt + "]";
	}
	
	
	
	
}
