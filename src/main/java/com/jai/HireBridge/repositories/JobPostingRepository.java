package com.jai.HireBridge.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jai.HireBridge.model.JobPosting;

public interface JobPostingRepository extends JpaRepository<JobPosting,Long> , JpaSpecificationExecutor<JobPosting>{

	List<JobPosting> findByRecruiterId(Long recruiterId);
}
