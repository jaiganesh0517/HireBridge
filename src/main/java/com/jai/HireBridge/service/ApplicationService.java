package com.jai.HireBridge.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jai.HireBridge.dto.ApplicantsResponse;
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
import com.jai.HireBridge.model.Users;
import com.jai.HireBridge.repositories.ApplicationRepository;
import com.jai.HireBridge.repositories.JobEligibleRepository;
import com.jai.HireBridge.repositories.JobPostingRepository;
import com.jai.HireBridge.repositories.StudentRepository;
import com.jai.HireBridge.repositories.UsersRepository;
import com.jai.HireBridge.statemachine.ApplicationStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ApplicationService 
{
   private ApplicationRepository appRepo;
   private JobPostingRepository jbRepo;
   private StudentRepository sPRepo;
   private JobEligibleRepository jERepo;
   private StudentRepository pRepo;
   private UsersRepository userRepo;
   private ApplicationStateMachine appMachine;
   private ResumeStorageService resumeStorage;
   private static final Logger log = LoggerFactory.getLogger(ApplicationService.class);
   
 
   

   public ApplicationService(ApplicationRepository appRepo, JobPostingRepository jbRepo, StudentRepository sPRepo,
			JobEligibleRepository jERepo, UsersRepository userRepo, ApplicationStateMachine appMachine,
			ResumeStorageService resumeStorage) {
		this.appRepo = appRepo;
		this.jbRepo = jbRepo;
		this.sPRepo = sPRepo;
		this.jERepo = jERepo;
		this.userRepo = userRepo;
		this.appMachine = appMachine;
		this.resumeStorage = resumeStorage;
	}

   public Application applyToJob(Long studentId, Long jobId ,MultipartFile file) {

	    JobPosting job = jbRepo.findById(jobId)
	            .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

	    if (job.getJobStatus() == Status.CLOSED) {
	        throw new BusinessRuleException("This job is closed.");
	    }

	    if (job.getDeadline().isBefore(LocalDateTime.now(ZoneId.of("Asia/Kolkata")))) {
	        throw new BusinessRuleException("Application deadline has passed for this job.");
	    }

	    if (appRepo.existsByStudentIdAndJobId(studentId, jobId)) {
	        throw new DuplicateResourceException("You are already registered for this job");
	    }

	    StudentProfile profile = sPRepo.findById(studentId)
	            .orElseThrow(() -> new ResourceNotFoundException("Your profile is not available"));
        
	    if (profile.getCgpa() < job.getMinCgpa()) {
	        throw new BusinessRuleException("CGPA is lower than expected");
	    }

	    if (!jERepo.existsByJobIdAndBranch(jobId, profile.getBranch())) {
	        throw new BusinessRuleException("Your branch is not eligible for this job");
	    }

	    String newKey;
	    String fileName;
	    String contentType;
	    Long size;

	    if (file != null && !file.isEmpty()) {
	        newKey = resumeStorage.upload(studentId, file);
	        fileName = file.getOriginalFilename();
	        contentType = file.getContentType();
	        size = file.getSize();
	    } else if (profile.getResumeKey() != null) {
	        newKey = resumeStorage.copy(profile.getResumeKey(), studentId);
	        fileName = profile.getResumeFileName();
	        contentType = profile.getResumeContentType();
	        size = profile.getResumeSize();
	    } else {
	        throw new BusinessRuleException("Please upload a resume");
	    }

	    Application app = new Application();
	    app.setStudentId(studentId);
	    app.setJobId(jobId);
	    app.setStatus(AppliStatus.APPLIED);
	    app.setAppliedAt(LocalDateTime.now());
	    app.setResumeKey(newKey);
	    app.setResumeFileName(fileName);
	    app.setResumeContentType(contentType);
	    app.setResumeSize(size);
	    app.setResumeUploadedAt(LocalDateTime.now());


	    try {
	        return appRepo.save(app);
	    } catch (RuntimeException e) {
	        try {
	            resumeStorage.delete(newKey);
	        } catch (RuntimeException ex) {
	            log.warn("Could not delete orphaned resume {}", newKey, ex);
	        }
	        if (e instanceof DataIntegrityViolationException) {
	            throw new DuplicateResourceException("You are already registered for this job");
	        }
	        throw e;
	    }
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
		 AppliStatus current = app.getStatus();
		 if (!appMachine.isValidTransition(current, newStatus)) {
		     throw new BusinessRuleException(
		         "Cannot change status from " + current + " to " + newStatus);
		 }
		 app.setStatus(newStatus);
		 appRepo.save(app);
		 return app;
   }
   
   public String getResumeUrl(Long callerId, Long applicationId) {
	    Application app = appRepo.findById(applicationId)
	            .orElseThrow(() -> new ResourceNotFoundException("No application found"));
	    JobPosting post = jbRepo.findById(app.getJobId())
	            .orElseThrow(() -> new ResourceNotFoundException("No job found"));

	    boolean isOwnerStudent = callerId.equals(app.getStudentId());
	    boolean isJobRecruiter = callerId.equals(post.getRecruiterId());
	    if (!isOwnerStudent && !isJobRecruiter) {
	        throw new UnauthorizedException("You don't have permission to view this resume");
	    }
	    if (app.getResumeKey() == null) {
	        throw new ResourceNotFoundException("No resume attached to this application");
	    }
	    return resumeStorage.presignedUrl(app.getResumeKey(), app.getResumeFileName());
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

	    MyApplicationResponse r = new MyApplicationResponse(
	            app.getApplicationId(),
	            app.getJobId(),
	            app.getStatus(),
	            app.getAppliedAt(),
	            job.getTitle(),
	            job.getDescrip(),
	            job.getJobStatus());

	    r.setResumeFileName(app.getResumeFileName());
	    r.setHasResume(app.getResumeKey() != null);
	    return r;
	}
   
   public List<ApplicantsResponse> getApplicantsForJob(Long jobId,Long recruiterId) {
	   Optional<JobPosting> jp = jbRepo.findById(jobId);
	   if(jp.isEmpty()) {
		   throw new ResourceNotFoundException("Job not found");
	   }
	   JobPosting post  = jp.get();
	   if(!recruiterId.equals(post.getRecruiterId())) {
		   throw new UnauthorizedException("Unauthorised");
	   }
	   List<Application> applicants = appRepo.findByJobId(jobId);
	   return applicants.stream()
			   .map(this::toApplicantResponse)
			   .collect(Collectors.toList());
   }
   
   private ApplicantsResponse toApplicantResponse(Application app) {
	   Optional<StudentProfile> profile = sPRepo.findById(app.getStudentId());
	   if(profile.isEmpty()) {
		   throw new ResourceNotFoundException("Profile Not Found");
	   }
	   StudentProfile profile1 = profile.get();
	   Optional<Users> user = userRepo.findById(app.getStudentId());
	   if(user.isEmpty()) {
		   throw new ResourceNotFoundException("User not found");
	   }
	   Users user1 = user.get();
	   
	   ApplicantsResponse r = new ApplicantsResponse(
		        user1.getUserName(),
		        profile1.getAbout(),
		        profile1.getBranch(),
		        profile1.getCgpa(),
		        profile1.getSkills(),
		        user1.getEmailId(),
		        app.getApplicationId(),
		        app.getStatus(),
		        app.getAppliedAt(),
		        app.getStudentId());

		r.setResumeFileName(app.getResumeFileName());
		r.setResumeSize(app.getResumeSize());
		r.setResumeUploadedAt(app.getResumeUploadedAt());
		r.setHasResume(app.getResumeKey() != null);
		return r;
	   
   }
}
