package com.example.securitydemoapp.repo;

import com.example.securitydemoapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByrol(String rol);
}

