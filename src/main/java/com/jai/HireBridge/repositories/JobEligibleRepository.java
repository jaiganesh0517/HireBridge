package com.jai.HireBridge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jai.HireBridge.model.JobEligibleBranch;

public interface JobEligibleRepository extends JpaRepository<JobEligibleBranch,Long>{

	boolean existsByJobIdAndBranch(Long jobId, String branch);
}
