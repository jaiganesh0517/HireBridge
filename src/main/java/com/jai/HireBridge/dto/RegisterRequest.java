package com.jai.HireBridge.dto;

import com.jai.HireBridge.model.Role;

import jakarta.validation.constraints.NotNull;

public class RegisterRequest 
{
    @NotNull
	private String userName;
	@NotNull
	private String emailId;
	@NotNull
	private String password;
	@NotNull
	private Role role;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	
}
