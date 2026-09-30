package com.jai.HireBridge.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jai.HireBridge.model.JobPosting;

public interface JobPostingRepository extends JpaRepository<JobPosting,Long> {

	List<JobPosting> findByRecruiterId(Long recruiterId);
}
