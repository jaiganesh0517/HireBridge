package com.jai.HireBridge.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jai.HireBridge.dto.StudentProfileRequest;
import com.jai.HireBridge.dto.StudentProfileResponse;
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
	
	public StudentProfileResponse getMyProfile(Long userId) {
		StudentProfile p = stuRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User profile does not exist"));
		StudentProfileResponse r = new StudentProfileResponse();
		r.setUserName(p.getUser().getUserName());
		r.setEmailId(p.getUser().getEmailId());
		r.setAbout(p.getAbout());
		r.setBatchYear(p.getBatchYear());
		r.setBranch(p.getBranch());
		r.setCgpa(p.getCgpa());
		r.setSkills(p.getSkills());
		return r;
		
	}
	

	public StudentProfile updateProfile(Long userId, StudentProfileRequest request) {
	    StudentProfile profile = stuRepo.findById(userId)
	        .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

	    profile.setAbout(request.getAbout());
	    profile.setBatchYear(request.getBatchYear());
	    profile.setBranch(request.getBranch());
	    profile.setCgpa(request.getCgpa());
	    profile.setSkills(request.getSkills());

	    return stuRepo.save(profile);
	}
}
