package com.jai.HireBridge.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jai.HireBridge.dto.JobPostingResponse;
import com.jai.HireBridge.exception.ResourceNotFoundException;
import com.jai.HireBridge.model.JobPosting;
import com.jai.HireBridge.model.SavedJob;
import com.jai.HireBridge.repositories.JobPostingRepository;
import com.jai.HireBridge.repositories.SavedJobsRepository;

import jakarta.transaction.Transactional;

@Service
public class SavedJobService {
	
	private JobPostingRepository jPRepo;
	private SavedJobsRepository sJRepo;
	public SavedJobService(JobPostingRepository jPRepo, SavedJobsRepository sJRepo) {
		super();
		this.jPRepo = jPRepo;
		this.sJRepo = sJRepo;
	}
	
	@Transactional 
	public void saveJob(Long studentId , Long jobId) {
		if(!jPRepo.existsById(jobId)) {
			throw new ResourceNotFoundException("Job Not Found");
		}
		if(sJRepo.existsByStudentIdAndJobId(studentId, jobId)) {
			return ;
		}
		sJRepo.save(new SavedJob(studentId,jobId));
	}
	
	@Transactional
	public void unsaveJob(Long studentId , Long jobId) {
		sJRepo.deleteByStudentIdAndJobId(studentId, jobId);
	}
	
	@Transactional
	public List<Long> getSavedJobIds(Long studentId){
		return sJRepo.findByStudentIdOrderBySavedAtDesc(studentId)
				.stream()
				.map(SavedJob::getJobId)
				.toList();
	}
	@Transactional
	public List<JobPostingResponse> getSavedJobs(Long studentId){
		List<Long> ids = getSavedJobIds(studentId);
		Map<Long, JobPosting> byId = jPRepo.findAllById(ids).stream()
				.collect(Collectors.toMap(JobPosting::getJobPostId, j ->j));
		
		return ids.stream()
				.filter(byId::containsKey)
				.map(id -> toResponse(byId.get(id)))
				.toList();
	}
	
	private JobPostingResponse toResponse(JobPosting j) {
        JobPostingResponse r = new JobPostingResponse();
        r.setJobPostId(j.getJobPostId());
        r.setTitle(j.getTitle());
        r.setDescrip(j.getDescrip());
        r.setCtc(j.getCtc());
        r.setMinCgpa(j.getMinCgpa());
        r.setDeadline(j.getDeadline());
        r.setJobStatus(j.getJobStatus());
        return r;
    }

}
