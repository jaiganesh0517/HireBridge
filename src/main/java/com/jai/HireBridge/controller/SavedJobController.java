package com.jai.HireBridge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jai.HireBridge.dto.JobPostingResponse;
import com.jai.HireBridge.service.SavedJobService;

@RestController
@RequestMapping("/api/saved-jobs")
public class SavedJobController {

	private final SavedJobService service;

	public SavedJobController(SavedJobService service) {
		super();
		this.service = service;
	}
	
	@PostMapping("/{jobId}")
	public ResponseEntity<Void> save(@PathVariable Long jobId, Authentication auth){
		service.saveJob((Long) auth.getPrincipal(), jobId);
        return ResponseEntity.noContent().build();		
	}
	
	@DeleteMapping("/{jobId}")
	public ResponseEntity<Void> unsave(@PathVariable Long jobId, Authentication auth){
		service.unsaveJob((Long) auth.getPrincipal(), jobId);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	public List<JobPostingResponse> list(Authentication auth){
		return service.getSavedJobs((Long) auth.getPrincipal());
	}
	
	@GetMapping("/ids")
	public List<Long> ids(Authentication auth){
		return service.getSavedJobIds((Long) auth.getPrincipal());
	}
	
	
}
