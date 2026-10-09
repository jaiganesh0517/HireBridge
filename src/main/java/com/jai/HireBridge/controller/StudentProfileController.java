package com.jai.HireBridge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jai.HireBridge.dto.StudentProfileRequest;
import com.jai.HireBridge.dto.StudentProfileResponse;
import com.jai.HireBridge.model.StudentProfile;
import com.jai.HireBridge.service.StudentProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentProfileController
{
  
	private StudentProfileService stuService;

	public StudentProfileController(StudentProfileService stuService) {
		super();
		this.stuService = stuService;
	}
	
	@PostMapping("/profile")
	public ResponseEntity<StudentProfile> studentProfile(@RequestBody StudentProfileRequest req ,Authentication auth){
		Long userId = (Long)auth.getPrincipal();
		StudentProfile profile = stuService.createStudentProfile(userId, req.getAbout(), req.getBranch(), req.getBatchYear(), req.getCgpa(), req.getSkills());
		return ResponseEntity.status(HttpStatus.CREATED).body(profile);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<StudentProfileResponse> getMyProfile(Authentication auth){
		Long userId = (Long) auth.getPrincipal();
		return ResponseEntity.ok(stuService.getMyProfile(userId));
	}
	@PutMapping("/profile")
	public ResponseEntity<StudentProfile> updateStudentProfile(Authentication auth, @RequestBody StudentProfileRequest request) {
	    Long userId = (Long) auth.getPrincipal();
	    return ResponseEntity.ok(stuService.updateProfile(userId, request));
	}
	
}
