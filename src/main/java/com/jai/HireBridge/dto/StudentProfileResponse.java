package com.jai.HireBridge.dto;

public class StudentProfileResponse 
{
  private String userName;
  private String emailId;
  private String about;
  private int batchYear;
  private String branch;
  private Double cgpa;
  private String skills;
  public String getUserName() {
	return userName;
  }
  public void setUserName(String userName) {
	this.userName = userName;
  }
  public String getEmailId() {
	return emailId;
  }
  public void setEmailId(String emailId) {
	this.emailId = emailId;
  }
  public String getAbout() {
	return about;
  }
  public void setAbout(String about) {
	this.about = about;
  }
  public int getBatchYear() {
	return batchYear;
  }
  public void setBatchYear(int bacthYear) {
	this.batchYear = bacthYear;
  }
  public String getBranch() {
	return branch;
  }
  public void setBranch(String branch) {
	this.branch = branch;
  }
  public Double getCgpa() {
	return cgpa;
  }
  public void setCgpa(Double cgpa) {
	this.cgpa = cgpa;
  }
  public String getSkills() {
	return skills;
  }
  public void setSkills(String skills) {
	this.skills = skills;
  }
  
  
}
