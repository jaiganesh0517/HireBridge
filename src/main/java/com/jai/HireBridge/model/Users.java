package com.jai.HireBridge.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Users
{
	@Id
	@GeneratedValue
    private Long userId;
	
    private String userName;
    
	@NotNull
	@Column(unique= true)
    private String emailId;
	
	@NotNull
    private String password;
	
	@Enumerated(EnumType.STRING)
	private Role userRole;
  
}
