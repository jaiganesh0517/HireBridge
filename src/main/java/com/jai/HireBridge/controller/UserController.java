package com.jai.HireBridge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jai.HireBridge.dto.LoginRequest;
import com.jai.HireBridge.dto.RegisterRequest;
import com.jai.HireBridge.model.Users;
import com.jai.HireBridge.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController
{
   private UserService userServ;

   public UserController(UserService userServ) {
	super();
	this.userServ = userServ;
   }
   
   @PostMapping("/register")
   public ResponseEntity<Users> register(@RequestBody RegisterRequest req){
	   Users user = userServ.register(req.getUserName(), req.getEmailId(), req.getPassword(), req.getRole());
	   return ResponseEntity.status(HttpStatus.CREATED).body(user);
   }
   
   @PostMapping("/login")
   public ResponseEntity<Users> login(@RequestBody LoginRequest req){
	   Users user = userServ.login(req.getEmailId(), req.getPassword());
	   return ResponseEntity.status(HttpStatus.OK).body(user);
   }
}
