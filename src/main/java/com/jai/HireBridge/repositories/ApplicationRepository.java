package com.jai.HireBridge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jai.HireBridge.model.Application;

public interface ApplicationRepository extends JpaRepository<Application,Long>
{
  boolean existsByStudentIdAndJobId(Long studentId,Long jobId);
} 
