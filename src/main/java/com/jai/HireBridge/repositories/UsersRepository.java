package com.jai.HireBridge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jai.HireBridge.model.Users;

public interface UsersRepository extends JpaRepository<Users,Long>
{
    Users findByEmailId(String emailId);
}
