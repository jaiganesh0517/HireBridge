package com.jai.HireBridge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping("/{jobId}/apply/{studentId}")
	public ResponseEntity<Application> applyToJob(@PathVariable Long jobId ,@PathVariable Long studentId){
		Application app = appService.applyToJob(studentId, jobId);
		return ResponseEntity.status(HttpStatus.CREATED).body(app);
	}
	
	@PatchMapping("/{recruiterId}/{applicationId}/status/{newStatus}")
	public ResponseEntity<String> updateApplicationStatus(@PathVariable Long recruiterId,@PathVariable Long applicationId , @PathVariable AppliStatus newStatus){
		appService.updateApplicationStatus(recruiterId, applicationId, newStatus);
		return ResponseEntity.status(HttpStatus.OK).body("Status updated successfully");
	}
}

