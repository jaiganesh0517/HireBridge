package com.jai.HireBridge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jai.HireBridge.dto.StudentProfileRequest;
import com.jai.HireBridge.model.StudentProfile;
import com.jai.HireBridge.service.StudentProfileService;

@RestController
@RequestMapping("/api/students/{userId}/profile")
public class StudentProfileController
{
  
	private StudentProfileService stuService;

	public StudentProfileController(StudentProfileService stuService) {
		super();
		this.stuService = stuService;
	}
	
	@PostMapping()
	public ResponseEntity<StudentProfile> studentProfile(@RequestBody StudentProfileRequest req ,@PathVariable Long userId){
		StudentProfile profile = stuService.createStudentProfile(userId, req.getAbout(), req.getBranch(), req.getBatchYear(), req.getCgpa(), req.getSkills());
		return ResponseEntity.status(HttpStatus.CREATED).body(profile);
	}
}
