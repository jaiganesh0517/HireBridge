package com.jai.HireBridge.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jai.HireBridge.model.SavedJob;

public interface SavedJobsRepository extends JpaRepository<SavedJob,Long>
{
    boolean existsByStudentIdAndJobId(Long studentId, Long jobId);
    void deleteByStudentIdAndJobId(Long studentId, Long jobId);
    List<SavedJob> findByStudentIdOrderBySavedAtDesc(Long studentId);
    
}
