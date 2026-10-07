package com.jai.HireBridge.service;

import org.springframework.stereotype.Service;

import com.jai.HireBridge.dto.RecruiterProfileRequest;
import com.jai.HireBridge.dto.RecruiterProfileResponse;
import com.jai.HireBridge.exception.DuplicateResourceException;
import com.jai.HireBridge.exception.ResourceNotFoundException;
import com.jai.HireBridge.model.RecruiterProfile;
import com.jai.HireBridge.model.StudentProfile;
import com.jai.HireBridge.model.Users;
import com.jai.HireBridge.repositories.RecruiterRepository;
import com.jai.HireBridge.repositories.UsersRepository;

@Service
public class RecruiterProfileService
{
 
	private RecruiterRepository recruiProfile;
	private UsersRepository userRepo;
	public RecruiterProfileService(RecruiterRepository recruiProfile, UsersRepository userRepo) {
		super();
		this.recruiProfile = recruiProfile;
		this.userRepo = userRepo;
	}
	
	public RecruiterProfile createRecruiterProfile(Long userId ,String companyName,String designation,String summary) {
		Users user = userRepo.findById(userId)
			    .orElseThrow(() -> new ResourceNotFoundException("User not exist"));

			if (recruiProfile.existsById(userId)) {
			    throw new DuplicateResourceException("Profile already existed");
			}

			RecruiterProfile profile = new RecruiterProfile(user, companyName, designation, summary);
			recruiProfile.save(profile);
			return profile;
	}
	
	public RecruiterProfileResponse getMyProfile(Long userId) {
		RecruiterProfile p = recruiProfile.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Profile not existed"));
		
		RecruiterProfileResponse r = new RecruiterProfileResponse();
		r.setUserName(p.getUser().getUserName());
		r.setEmailId(p.getUser().getEmailId());
		r.setCompanyName(p.getCompanyName());
		r.setDesignation(p.getDesignation());
		r.setSummary(p.getSummary());
		return r;
	}
	public RecruiterProfile updateProfile(Long userId, RecruiterProfileRequest request) {
	    RecruiterProfile profile = recruiProfile.findById(userId)
	        .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

	    profile.setCompanyName(request.getCompanyName());
	    profile.setDesignation(request.getDesignation());
	    profile.setSummary(request.getSummary());

	    return recruiProfile.save(profile);
	}
}