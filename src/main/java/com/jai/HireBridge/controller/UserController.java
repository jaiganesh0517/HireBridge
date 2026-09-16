package com.jai.HireBridge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jai.HireBridge.dto.AuthResponse;
import com.jai.HireBridge.dto.LoginRequest;
import com.jai.HireBridge.dto.RegisterRequest;
import com.jai.HireBridge.model.Users;
import com.jai.HireBridge.config.JwtUtil;
import com.jai.HireBridge.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private UserService userServ;
    private JwtUtil jwtUtil;

    public UserController(UserService userServ, JwtUtil jwtUtil) {
        super();
        this.userServ = userServ;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody RegisterRequest req) {
        Users user = userServ.register(req.getUserName(), req.getEmailId(), req.getPassword(), req.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        Users user = userServ.login(req.getEmailId(), req.getPassword());
        String token = jwtUtil.generateToken(user.getUserId(), user.getUserRole().name());
        AuthResponse response = new AuthResponse(user.getUserId(), user.getUserRole().name(), token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}