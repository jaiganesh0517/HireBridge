package com.jai.HireBridge.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jai.HireBridge.exception.ResourceNotFoundException;
import com.jai.HireBridge.exception.UnauthorizedException;
import com.jai.HireBridge.model.JobEligibleBranch;
import com.jai.HireBridge.model.JobPosting;
import com.jai.HireBridge.model.Status;
import com.jai.HireBridge.repositories.JobEligibleRepository;
import com.jai.HireBridge.repositories.JobPostingRepository;

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
}
