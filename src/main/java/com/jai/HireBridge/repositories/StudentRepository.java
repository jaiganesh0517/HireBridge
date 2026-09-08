package com.jai.HireBridge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jai.HireBridge.model.StudentProfile;

public interface StudentRepository extends JpaRepository<StudentProfile,Long>{

}
