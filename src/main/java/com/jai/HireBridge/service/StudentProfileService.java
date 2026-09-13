package com.jai.HireBridge.service;

import org.springframework.stereotype.Service;

import com.jai.HireBridge.model.StudentProfile;
import com.jai.HireBridge.repositories.StudentRepository;

@Service
public class StudentProfileService
{
   
	private StudentRepository stuRepo;

	public StudentProfileService(StudentRepository stuRepo) {
		super();
		this.stuRepo = stuRepo;
	}
	
	public StudentProfile createStudentProfile(Long userId , String about,String branch,int batchYear,Double cgpa ,String skills){
		if(stuRepo.existsById(userId)) {
			throw new RuntimeException("Profile already existed");
		}
		StudentProfile profile = new StudentProfile();
		profile.setAbout(about);
		profile.setBatchYear(batchYear);
		profile.setBranch(branch);
		profile.setCgpa(cgpa);
		profile.setSkills(skills);
		profile.setUserId(userId);
		
		stuRepo.save(profile);
		
		return profile;
		
	}
}
