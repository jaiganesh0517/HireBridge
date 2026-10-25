package com.jai.HireBridge.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jai.HireBridge.dto.ApplicantsResponse;
import com.jai.HireBridge.dto.MyApplicationResponse;
import com.jai.HireBridge.model.AppliStatus;
import com.jai.HireBridge.model.Application;
import com.jai.HireBridge.service.ApplicationService;

@RestController
@RequestMapping("/api/application")
public class ApplicationController {

	private ApplicationService appService;

	public ApplicationController(ApplicationService appService) {
		super();
		this.appService = appService;
	}
	
	@PostMapping("/{jobId}/apply")
	public ResponseEntity<Map<String, Object>> apply(@PathVariable Long jobId,
	        @RequestParam(value = "file", required = false) MultipartFile file,
	        Authentication auth) {
	    Long studentId = (Long) auth.getPrincipal();
	    Application app = appService.applyToJob(studentId, jobId, file);
	    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
	            "applicationId", app.getApplicationId(),
	            "status", app.getStatus()));
	}
	
	@PatchMapping("/{applicationId}/status/{newStatus}")
	public Application updateApplicationStatus(@PathVariable Long applicationId , @PathVariable AppliStatus newStatus,Authentication authentication){
		Long recruiterId = (Long) authentication.getPrincipal();
		Application app = appService.updateApplicationStatus(recruiterId, applicationId, newStatus);
		return app;
	}
	
	@GetMapping("/{applicationId}/resume")
	public ResponseEntity<Map<String, String>> getResume(@PathVariable Long applicationId, Authentication auth) {
	    Long userId = (Long) auth.getPrincipal();
	    return ResponseEntity.ok(Map.of("url", appService.getResumeUrl(userId, applicationId)));
	}
	
	@GetMapping("/myApplications")
	public List<MyApplicationResponse> getMyApplications(Authentication auth){
		return appService.getMyApplication((Long)auth.getPrincipal());
	}
	
	@GetMapping("/{jobId}/applicants")
	public List<ApplicantsResponse> getAllApplicants(@PathVariable Long jobId ,Authentication auth){
		Long recruiterId = (Long) auth.getPrincipal();
		return appService.getApplicantsForJob(jobId, recruiterId);
	}
}

