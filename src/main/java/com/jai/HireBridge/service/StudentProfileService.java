package com.jai.HireBridge.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	private ResumeStorageService resumeStorage;

	
	public StudentProfileService(StudentRepository stuRepo, UsersRepository userRepo,ResumeStorageService resumeStorage) {
		super();
		this.stuRepo = stuRepo;
		this.userRepo = userRepo;
		this.resumeStorage = resumeStorage;
	}


	public StudentProfileResponse createStudentProfile(Long userId , String about,String branch,int batchYear,Double cgpa ,String skills){
		    Users user = userRepo.findById(userId)
			    .orElseThrow(() -> new ResourceNotFoundException("User not exist"));

			if (stuRepo.existsById(userId)) {
			    throw new DuplicateResourceException("Profile already existed");
			}

			StudentProfile profile = new StudentProfile(user,about,branch,batchYear,cgpa,skills, null, null, null, null, null);
			stuRepo.save(profile);
			return toResponse(profile);	
	}
	
	public StudentProfileResponse getMyProfile(Long userId) {
		StudentProfile p = stuRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User profile does not exist"));
		
		return toResponse(p);
		
	}
	

	public StudentProfileResponse updateProfile(Long userId, StudentProfileRequest request) {
	    StudentProfile profile = stuRepo.findById(userId)
	        .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

	    profile.setAbout(request.getAbout());
	    profile.setBatchYear(request.getBatchYear());
	    profile.setBranch(request.getBranch());
	    profile.setCgpa(request.getCgpa());
	    profile.setSkills(request.getSkills());
	    stuRepo.save(profile);
	    return toResponse(profile);
	}
	public StudentProfileResponse uploadDefaultResume(Long userId, MultipartFile file) {
		StudentProfile profile = stuRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Profile not exist"));
		String oldKey = profile.getResumeKey();
		String newKey = resumeStorage.upload(userId, file);
		profile.setResumeContentType(file.getContentType());
		profile.setResumeFileName(file.getOriginalFilename());
		profile.setResumeKey(newKey);
		profile.setResumeSize(file.getSize());
		profile.setResumeUploadedAt(LocalDateTime.now());
		try {
			stuRepo.save(profile);
		}catch(RuntimeException e) {
			resumeStorage.delete(newKey);
			throw e;
		}
		try {
		    resumeStorage.delete(oldKey);
		}catch(RuntimeException ignored) {
			
		}
	    
		return toResponse(profile);
	}
	private StudentProfileResponse toResponse(StudentProfile s) {
		StudentProfileResponse response =  new StudentProfileResponse();
		response.setUserName(s.getUser().getUserName());
		response.setEmailId(s.getUser().getEmailId());
		response.setAbout(s.getAbout());
		response.setBatchYear(s.getBatchYear());
		response.setBranch(s.getBranch());
		response.setCgpa(s.getCgpa());
		response.setSkills(s.getSkills());
		response.setResumeFileName(s.getResumeFileName());
		response.setResumeSize(s.getResumeSize());
		response.setResumeUploadedAt(s.getResumeUploadedAt());
		return response;
	}
	
	public String getMyResumeUrl(Long userId) {
		StudentProfile profile = stuRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Profile not exist"));

		if (profile.getResumeKey() == null) {
			throw new ResourceNotFoundException("No resume uploaded yet");
		}
		return resumeStorage.presignedUrl(profile.getResumeKey(), profile.getResumeFileName());
	}
}
