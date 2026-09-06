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
public class StudentProfile 
{
	@Id
    private Long userId;
    private String about;
    private String branch;
    private int batchYear;
    private double cgpa;
    private String skills;
}
