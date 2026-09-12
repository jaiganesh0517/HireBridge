package com.jai.HireBridge.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JobPostingRequest 
{
  private String title;
  private String descrip;
  private BigDecimal ctc;
  private double minCgpa;
  private LocalDateTime deadline;
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
  
  
}
