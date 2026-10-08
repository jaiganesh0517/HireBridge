package com.jai.HireBridge.repositories;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jai.HireBridge.model.JobPosting;
import com.jai.HireBridge.model.Status;

public interface JobPostingRepository extends JpaRepository<JobPosting,Long> , JpaSpecificationExecutor<JobPosting>{

	List<JobPosting> findByRecruiterId(Long recruiterId);
	
	@Query("""
		    SELECT j FROM JobPosting j
		    WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))
		      AND LOWER(j.descrip) LIKE LOWER(CONCAT('%', :skill, '%'))
		    """)
		Page<JobPosting> searchJobs(@Param("title") String title,
		                            @Param("skill") String skill,
		                            Pageable pageable);
	
	@Modifying
	@Query("""
	    UPDATE JobPosting j
	    SET j.jobStatus = :closed
	    WHERE j.jobStatus = :open
	      AND j.deadline < :now
	    """)
	int closeExpiredJobs(@Param("open") Status open,
	                     @Param("closed") Status closed,
	                     @Param("now") LocalDateTime now);
}
