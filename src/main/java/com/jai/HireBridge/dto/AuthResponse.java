package com.jai.HireBridge.dto;
import com.jai.HireBridge.model.Role;

public class AuthResponse {
    private Long userId;
    private String userName;
    private Role role;
    private String token;

    public AuthResponse(Long userId, Role role, String userName, String token) {
        this.userId = userId;
        this.role = role;
        this.userName = userName;
        this.token = token;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}