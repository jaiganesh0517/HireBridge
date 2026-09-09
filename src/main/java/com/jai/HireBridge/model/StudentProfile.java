package com.jai.HireBridge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfile 
{
	@Id
    private Long userId;
    private String about;
    private String branch;
    private int batchYear;
    private double cgpa;
    private String skills;
    
    
	public StudentProfile() {
		super();
	}
	public StudentProfile(Long userId, String about, String branch, int batchYear, double cgpa, String skills) {
		super();
		this.userId = userId;
		this.about = about;
		this.branch = branch;
		this.batchYear = batchYear;
		this.cgpa = cgpa;
		this.skills = skills;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	@Override
	public String toString() {
		return "StudentProfile [userId=" + userId + ", about=" + about + ", branch=" + branch + ", batchYear="
				+ batchYear + ", cgpa=" + cgpa + ", skills=" + skills + "]";
	}
    
    
}
