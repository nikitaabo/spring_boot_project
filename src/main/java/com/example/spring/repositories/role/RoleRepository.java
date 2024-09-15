package com.example.spring.repositories.role;

import com.example.spring.models.Role;
import com.example.spring.models.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(RoleName roleName);
}
