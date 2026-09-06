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
}
