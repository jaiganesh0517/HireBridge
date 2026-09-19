package com.jai.HireBridge.model;

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
    
    
	public StudentProfile() {
		super();
	}
	public StudentProfile(Users user, String about, String branch, int batchYear, double cgpa, String skills) {
		super();
		this.user = user;
		this.about = about;
		this.branch = branch;
		this.batchYear = batchYear;
		this.cgpa = cgpa;
		this.skills = skills;
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
	@Override
	public String toString() {
		return "StudentProfile [userId=" + user.getUserId() + ", about=" + about + ", branch=" + branch + ", batchYear="
				+ batchYear + ", cgpa=" + cgpa + ", skills=" + skills + "]";
	}
    
    
}
