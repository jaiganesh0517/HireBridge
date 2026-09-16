package com.jai.HireBridge.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jai.HireBridge.dto.JobPostingRequest; 
import com.jai.HireBridge.model.JobPosting;
import com.jai.HireBridge.service.JobPostingService;

@RestController
@RequestMapping("/api")
public class JobPostingController
{
	private JobPostingService jBPService;
	
	

	public JobPostingController(JobPostingService jBPService) {
		super();
		this.jBPService = jBPService;
	}



	@PostMapping("/jobs/{recruiterId}")
	public ResponseEntity<JobPosting> createJobPost(@RequestBody JobPostingRequest req,@PathVariable Long recruiterId){
		JobPosting post = jBPService.createJobPosting(recruiterId, req.getTitle(), req.getDescrip(), req.getCtc(), req.getMinCgpa(), req.getDeadline());
		return ResponseEntity.status(HttpStatus.CREATED).body(post);
	}
	
	@PostMapping("/jobs/{jobId}/branches")
	public ResponseEntity<String> addEligibleBranch(@RequestBody List<String>branches,@PathVariable Long jobId){
		jBPService.addEligibleBranches(jobId,branches);
		return ResponseEntity.status(HttpStatus.CREATED).body("Branches added successfully");
	}
	
}
