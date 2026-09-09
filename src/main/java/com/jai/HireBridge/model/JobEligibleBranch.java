package com.jai.HireBridge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobEligibleBranch
{
  @Id
  @GeneratedValue
  private Long id;
  private Long jobId;
  @NotNull
  private String branch;
  
  
  public JobEligibleBranch() {
	super();
}
  public JobEligibleBranch(Long id, Long jobId, @NotNull String branch) {
	super();
	this.id = id;
	this.jobId = jobId;
	this.branch = branch;
  }
  public Long getId() {
	return id;
  }
  public void setId(Long id) {
	this.id = id;
  }
  public Long getJobId() {
	return jobId;
  }
  public void setJobId(Long jobId) {
	this.jobId = jobId;
  }
  public String getBranch() {
	return branch;
  }
  public void setBranch(String branch) {
	this.branch = branch;
  }
  @Override
  public String toString() {
	return "JobEligibleBranch [id=" + id + ", jobId=" + jobId + ", branch=" + branch + "]";
  }
  
  
}
