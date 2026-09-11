package com.jai.HireBridge.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.jai.HireBridge.model.Role;

import com.jai.HireBridge.model.Users;
import com.jai.HireBridge.repositories.UsersRepository;

@Service
public class UserService 
{
  private UsersRepository uRepo;
  private PasswordEncoder passEnco;
  public UserService(UsersRepository uRepo ,PasswordEncoder passEnco) {
	super();
	this.uRepo = uRepo;
	this.passEnco = passEnco;
  }
  
  public Users register(String userName, String emailId, String password , Role role) 
  {
	  Users user = new Users();
	  user.setUserName(userName);
	  Users user1 = new Users();
	  user1 = uRepo.findByEmailId(emailId);
	  if(user1 != null) {
		 throw new RuntimeException("Email already registered"); 
	  }
	  user.setEmailId(emailId);
	  String hashPass = passEnco.encode(password);
	  user.setPassword(hashPass);
	  user.setUserRole(role);
	  uRepo.save(user);
	  return user;
  }


  public Users login(String emailId , String Password) 
  {
	Users user = uRepo.findByEmailId(emailId);
	if(user == null) {
		throw new RuntimeException("Invalid email or password");
	}
	
	if(!passEnco.matches(Password, user.getPassword())) {
		throw new RuntimeException("Invalid emial or password");
	}
	return user;
  }
  
  }