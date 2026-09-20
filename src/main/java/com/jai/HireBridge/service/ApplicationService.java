package com.jai.HireBridge.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jai.HireBridge.dto.MyApplicationResponse;
import com.jai.HireBridge.exception.BusinessRuleException;
import com.jai.HireBridge.exception.DuplicateResourceException;
import com.jai.HireBridge.exception.ResourceNotFoundException;
import com.jai.HireBridge.exception.UnauthorizedException;
import com.jai.HireBridge.model.AppliStatus;
import com.jai.HireBridge.model.Application;
import com.jai.HireBridge.model.JobPosting;
import com.jai.HireBridge.model.Status;
import com.jai.HireBridge.model.StudentProfile;
import com.jai.HireBridge.repositories.ApplicationRepository;
import com.jai.HireBridge.repositories.JobEligibleRepository;
import com.jai.HireBridge.repositories.JobPostingRepository;
import com.jai.HireBridge.repositories.StudentRepository;

@Service
public class ApplicationService 
{
   private ApplicationRepository appRepo;
   private JobPostingRepository jbRepo;
   private StudentRepository sPRepo;
   private JobEligibleRepository jERepo;
   
   public ApplicationService(ApplicationRepository appRepo, JobPostingRepository jbRepo, StudentRepository sPRepo,
		JobEligibleRepository jERepo) {
	super();
	this.appRepo = appRepo;
	this.jbRepo = jbRepo;
	this.sPRepo = sPRepo;
	this.jERepo = jERepo;
}






   public Application applyToJob(Long studentId ,Long jobId) {
	   
	   Optional<JobPosting> jPost = jbRepo.findById(jobId);
	   if(jPost.isEmpty()) {
		   throw new ResourceNotFoundException("Job not found");
	   }
	   
	   JobPosting job = jPost.get();
	   
	   
	   Status stat = job.getJobStatus();
	   if(stat.equals(Status.CLOSED)) {
		   throw new BusinessRuleException("Application for this job is closed");
	   }
	   
	   if(appRepo.existsByStudentIdAndJobId(studentId, jobId)) {
		   throw new DuplicateResourceException("You are already registered for this job");
	   }
	   Optional<StudentProfile> sProfile = sPRepo.findById(studentId);
	   StudentProfile profile;
	   if(sProfile.isPresent()) {
	       profile = sProfile.get();
	       if(profile.getCgpa() >= job.getMinCgpa()) {
	    	   if(!jERepo.existsByJobIdAndBranch(jobId, profile.getBranch())) {
	    		   throw new BusinessRuleException("Your branch is not eligible for this job");
	    	   }
	       }else {
	    	   throw new BusinessRuleException("CGPA is lower than expected");
	       }
	   }else {
	              throw new ResourceNotFoundException("Your profile is not available");
	        }
	   Application app = new Application();
	   app.setStudentId(studentId);
	   app.setJobId(jobId);
	   app.setStatus(AppliStatus.APPLIED);
	   app.setAppliedAt(LocalDateTime.now());
	   appRepo.save(app);
	   return app;
   }
   
   public Application updateApplicationStatus(Long recruiterId ,Long applicationId ,AppliStatus newStatus) {
		 Optional<Application> application = appRepo.findById(applicationId); 
		 if(application.isEmpty()) {
			 throw new ResourceNotFoundException("NO Application found");
		 }
		 Application app = application.get();
		 Long id = app.getJobId();
		 Optional<JobPosting> jobPost = jbRepo.findById(id);
		 if(jobPost.isEmpty()) {
			 throw new ResourceNotFoundException("NO job found");
		 }
		 JobPosting post = jobPost.get();
		 if(!recruiterId.equals(post.getRecruiterId())) {
			 throw new UnauthorizedException("You don't have permision to change this application");
		 }
		 app.setStatus(newStatus);
		 appRepo.save(app);
		 return app;
   }
   
   public List<MyApplicationResponse> getMyApplication(Long studentId){
	   List<Application> application = appRepo.findByStudentId(studentId);
	   
	   return application.stream()
			   .map(this::toMyApplicationResponse)
			   .collect(Collectors.toList());
   }
    
   private MyApplicationResponse toMyApplicationResponse(Application app) {
	   JobPosting job = jbRepo.findById(app.getJobId())
			   .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
   
	   return new MyApplicationResponse(
			   app.getApplicationId(),
			   app.getJobId(),
			   app.getStatus(),
			   app.getAppliedAt(),
			   job.getTitle(),
			   job.getDescrip(),
			   job.getJobStatus()
			   );
	   
   }
}
