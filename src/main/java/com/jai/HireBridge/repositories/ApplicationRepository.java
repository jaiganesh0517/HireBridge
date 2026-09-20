package com.jai.HireBridge.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jai.HireBridge.model.Application;

public interface ApplicationRepository extends JpaRepository<Application,Long>
{
  boolean existsByStudentIdAndJobId(Long studentId,Long jobId);
  List<Application> findByStudentId(Long studentId);
} 
