package com.jai.HireBridge.dto;

public class StudentProfileRequest 
{
 
	private String about;
	private int bacthYear;
	private String branch;
	private double cgpa;
	private String skills;
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public int getBatchYear() {
		return bacthYear;
	}
	public void setBatchYear(int bacthYear) {
		this.bacthYear = bacthYear;
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
	
	
}
