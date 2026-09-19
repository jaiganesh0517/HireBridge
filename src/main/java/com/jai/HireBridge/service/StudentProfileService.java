package com.jai.HireBridge.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jai.HireBridge.exception.DuplicateResourceException;
import com.jai.HireBridge.exception.ResourceNotFoundException;
import com.jai.HireBridge.model.StudentProfile;
import com.jai.HireBridge.model.Users;
import com.jai.HireBridge.repositories.StudentRepository;
import com.jai.HireBridge.repositories.UsersRepository;

@Service
public class StudentProfileService
{
   
	private StudentRepository stuRepo;
	private UsersRepository userRepo;

	
	public StudentProfileService(StudentRepository stuRepo, UsersRepository userRepo) {
		super();
		this.stuRepo = stuRepo;
		this.userRepo = userRepo;
	}


	public StudentProfile createStudentProfile(Long userId , String about,String branch,int batchYear,Double cgpa ,String skills){
		    Users user = userRepo.findById(userId)
			    .orElseThrow(() -> new ResourceNotFoundException("User not exist"));

			if (stuRepo.existsById(userId)) {
			    throw new DuplicateResourceException("Profile already existed");
			}

			StudentProfile profile = new StudentProfile(user, about, branch, batchYear, cgpa, skills);
			stuRepo.save(profile);
			return profile;
		
	}
}
