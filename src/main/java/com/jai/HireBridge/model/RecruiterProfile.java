package com.jai.HireBridge.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
public class RecruiterProfile 
{
	@Id
	@Column(name = "user_id")
	private Long userId;

	@OneToOne
	@MapsId
	@JoinColumn(name = "user_id")
	private Users user;
	
	private String companyName;
	
	private String designation;
	
	private String summary;
 
	
	public RecruiterProfile() {
		super();
	}

	public RecruiterProfile(Users user, String companyName, String designation, String summary) {
		super();
		this.user = user;
		this.companyName = companyName;
		this.designation = designation;
		this.summary = summary;
	}

	public Users getUserId() {
		return user;
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
		return "RecruiterProfile [userId=" + user + ", companyName=" + companyName + ", designation=" + designation
				+ ", summary=" + summary + "]";
	}
	
	
}
