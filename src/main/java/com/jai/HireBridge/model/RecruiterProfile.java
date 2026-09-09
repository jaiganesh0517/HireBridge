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
public class RecruiterProfile 
{
	@Id
    private Long userId;
	
	private String companyName;
	
	private String designation;
	
	private String summary;
 
	
	public RecruiterProfile() {
		super();
	}

	public RecruiterProfile(Long userId, String companyName, String designation, String summary) {
		super();
		this.userId = userId;
		this.companyName = companyName;
		this.designation = designation;
		this.summary = summary;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public String toString() {
		return "RecruiterProfile [userId=" + userId + ", companyName=" + companyName + ", designation=" + designation
				+ ", summary=" + summary + "]";
	}
	
	
}
