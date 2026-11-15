package com.jai.HireBridge.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jai.HireBridge.dto.JobPostingResponse;
import com.jai.HireBridge.dto.PageResponse;
import com.jai.HireBridge.exception.ResourceNotFoundException;
import com.jai.HireBridge.exception.UnauthorizedException;
import com.jai.HireBridge.model.JobEligibleBranch;
import com.jai.HireBridge.model.JobPosting;
import com.jai.HireBridge.model.RecruiterProfile;
import com.jai.HireBridge.model.Status;
import com.jai.HireBridge.model.Users;
import com.jai.HireBridge.repositories.JobEligibleRepository;
import com.jai.HireBridge.repositories.JobPostingRepository;

import jakarta.persistence.criteria.Join;

@Service
public class JobPostingService 
{
  private JobPostingRepository jpRepo;
  private JobEligibleRepository jERepo;
  
  
  public JobPostingService(JobPostingRepository jpRepo, JobEligibleRepository jERepo) {
	super();
	this.jpRepo = jpRepo;
	this.jERepo = jERepo;
}

  public JobPosting createJobPosting(Long recruiterId , String title , String descrip , BigDecimal ctc , double cgpa ,LocalDateTime deadline) 
  {
	  JobPosting jobPost =  new JobPosting() ;
	  jobPost.setRecruiterId(recruiterId);
	  jobPost.setTitle(title);
	  jobPost.setDescrip(descrip);
	  jobPost.setCtc(ctc);
	  jobPost.setMinCgpa(cgpa);
	  jobPost.setJobStatus(Status.OPEN);
	  jobPost.setDeadline(deadline);
	  jpRepo.save(jobPost);
	  return jobPost;
  }
  
  public void addEligibleBranches(Long jobId ,Long recruiterId, List<String> branches) {
	  Optional<JobPosting> jPost = jpRepo.findById(jobId);
	  JobPosting post ;
	  if(jPost.isPresent()) {
		  post = jPost.get();
		  if(post.getRecruiterId().equals(recruiterId)) {
			  for(String branch : branches) 
			  {
				JobEligibleBranch brnch = new JobEligibleBranch();  
				brnch.setJobId(jobId);
				brnch.setBranch(branch);
				jERepo.save(brnch);
			  }  
		  }else {
			  throw new UnauthorizedException("You did't have access to modify");
		  }
	  }else {
		  throw new ResourceNotFoundException("Resource Not Found");
	  }
  }
  
  
  public List<JobPostingResponse> getAllJobs(){
	  
	  return jpRepo.findAll()
			  .stream()
			  .map(this::toResponse)
			  .collect(Collectors.toList());
  }
  public JobPostingResponse toResponse(JobPosting job) {
	  return new JobPostingResponse(
			  job.getJobPostId(),
			  job.getTitle(),
			  job.getDescrip(),
			  job.getCtc(),
			  job.getMinCgpa(),
			  job.getDeadline(),
			  job.getJobStatus()
			  );	  
  }
  
  public List<JobPostingResponse> getJobsByRecruiter(Long recruiterId){
	  List<JobPosting> jobs = jpRepo.findByRecruiterId(recruiterId);
	  return jobs.stream()
			  .map(this::toResponse)
			  .toList();
  }
  
  public List<JobPostingResponse> searchJobs(String title, String skill) {
	    Specification<JobPosting> spec = (root, query, cb) -> cb.conjunction();

	    if (title != null && !title.isBlank()) {
	        spec = spec.and((root, query, cb) ->
	            cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
	    }

	    if (skill != null && !skill.isBlank()) {
	        spec = spec.and((root, query, cb) ->
	            cb.like(cb.lower(root.get("descrip")), "%" + skill.toLowerCase() + "%"));
	    }

	    return jpRepo.findAll(spec)
	        .stream()
	        .map(job -> new JobPostingResponse(
	            job.getJobPostId(),
	            job.getTitle(),
	            job.getDescrip(),
	            job.getCtc(),
	            job.getMinCgpa(),
	            job.getDeadline(),
	            job.getJobStatus()
	        ))
	        .toList();
	}
  public PageResponse<JobPostingResponse> getAllJobsPaged(int page, int size) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by("jobPostId").descending()); // verify field name
	    Page<JobPosting> result = jpRepo.findAll(pageable);
	    return toPageResponse(result);
	}

	public PageResponse<JobPostingResponse> searchJobsPaged(String title, String skill, int page, int size) {
	    String t = (title == null) ? "" : title.trim();
	    String s = (skill == null) ? "" : skill.trim();
	    Pageable pageable = PageRequest.of(page, size, Sort.by("jobPostId").descending());
	    Page<JobPosting> result = jpRepo.searchJobs(t, s, pageable);
	    return toPageResponse(result);
	}

	private PageResponse<JobPostingResponse> toPageResponse(Page<JobPosting> result) {
	    List<JobPostingResponse> content = result.getContent().stream()
	        .map(this::toResponse)       // verify: use your existing entity-to-DTO mapping
	        .toList();

	    return new PageResponse<>(content, result.getNumber(), result.getSize(),
	            result.getTotalElements(), result.getTotalPages(), result.isLast());
	}
}
