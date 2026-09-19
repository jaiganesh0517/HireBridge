package com.jai.HireBridge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jai.HireBridge.dto.RecruiterProfileRequest;
import com.jai.HireBridge.model.RecruiterProfile;
import com.jai.HireBridge.model.StudentProfile;
import com.jai.HireBridge.service.RecruiterProfileService;


@RestController
@RequestMapping("/api/recruiter/profile")
public class RecruiterProfileController {
	
	private RecruiterProfileService service;

	public RecruiterProfileController(RecruiterProfileService service) {
		super();
		this.service = service;
	}
	
	
    @PostMapping()
    public ResponseEntity<RecruiterProfile> createRecruiterProfile(@RequestBody RecruiterProfileRequest req , Authentication auth) {
    	Long recruiterId =  (Long) auth.getPrincipal();
    	RecruiterProfile profile = service.createRecruiterProfile(recruiterId, req.getCompanyName(), req.getDesignation(), req.getSummary());
    	return ResponseEntity.status(HttpStatus.CREATED).body(profile);
 }
}
