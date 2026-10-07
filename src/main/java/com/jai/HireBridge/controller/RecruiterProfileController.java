package com.jai.HireBridge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jai.HireBridge.dto.RecruiterProfileRequest;
import com.jai.HireBridge.dto.RecruiterProfileResponse;
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
    
    @GetMapping()
    public ResponseEntity<RecruiterProfileResponse> getMyProfile(Authentication auth){
    	Long userId = (Long) auth.getPrincipal();
    	return ResponseEntity.ok(service.getMyProfile(userId));
    }
    
    @PutMapping("/profile")
    public ResponseEntity<RecruiterProfile> updateRecruiterProfile(Authentication auth, @RequestBody RecruiterProfileRequest request) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(service.updateProfile(userId, request));
    }
}
