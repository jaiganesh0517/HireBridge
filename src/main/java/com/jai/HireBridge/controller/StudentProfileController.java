package com.jai.HireBridge.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jai.HireBridge.dto.StudentProfileRequest;
import com.jai.HireBridge.dto.StudentProfileResponse;
import com.jai.HireBridge.service.StudentProfileService;



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
	public ResponseEntity<StudentProfileResponse> studentProfile(@RequestBody StudentProfileRequest req ,Authentication auth){
		Long userId = (Long)auth.getPrincipal();
		StudentProfileResponse profile = stuService.createStudentProfile(userId, req.getAbout(), req.getBranch(), req.getBatchYear(), req.getCgpa(), req.getSkills());
		return ResponseEntity.status(HttpStatus.CREATED).body(profile);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<StudentProfileResponse> getMyProfile(Authentication auth){
		Long userId = (Long) auth.getPrincipal();
		return ResponseEntity.ok(stuService.getMyProfile(userId));
	}
	@PutMapping("/profile")
	public ResponseEntity<StudentProfileResponse> updateStudentProfile(Authentication auth, @RequestBody StudentProfileRequest request) {
	    Long userId = (Long) auth.getPrincipal();
	    return ResponseEntity.ok(stuService.updateProfile(userId, request));
	}
	@PostMapping(value = "/resume",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadDefaultResume(@RequestParam("file") MultipartFile file,Authentication auth){
		Long userId = (Long) auth.getPrincipal();
		StudentProfileResponse response = stuService.uploadDefaultResume(userId, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/resume")
	public ResponseEntity<Map<String, String>> getMyResume(Authentication auth) {
		Long userId = (Long) auth.getPrincipal();
		return ResponseEntity.ok(Map.of("url", stuService.getMyResumeUrl(userId)));
	}
	
}