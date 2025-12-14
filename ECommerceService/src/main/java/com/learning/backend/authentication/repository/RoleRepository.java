package com.learning.backend.authentication.repository;

import com.learning.backend.authentication.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
