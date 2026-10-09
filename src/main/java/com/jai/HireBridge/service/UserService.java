package com.jai.HireBridge.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jai.HireBridge.exception.BusinessRuleException;
import com.jai.HireBridge.exception.DuplicateResourceException;
import com.jai.HireBridge.exception.UnauthorizedException;
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
  
  private static final String PASSWORD_PATTERN = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{6,}$";
  public boolean isValid(String password) {
	  if(password == null) {
		  return false;
	  }
	  return password.matches(PASSWORD_PATTERN);
  }
  public Users register(String userName, String emailId, String password , Role role) 
  {
	  Users user = new Users();
	  user.setUserName(userName);
	  Users user1 = new Users();
	  user1 = uRepo.findByEmailId(emailId);
	  if(user1 != null) {
		 throw new DuplicateResourceException("Email already registered"); 
	  }
	  user.setEmailId(emailId);
	  if(!isValid(password)) {
		  throw new BusinessRuleException("Password must be at least 6 characters long and atleast have one special ");
	  }
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
		throw new BusinessRuleException("Invalid email or password");
	}
	
	if(!passEnco.matches(Password, user.getPassword())) {
		throw new BusinessRuleException("Invalid email or password");
	}
	return user;
  }
  
  }