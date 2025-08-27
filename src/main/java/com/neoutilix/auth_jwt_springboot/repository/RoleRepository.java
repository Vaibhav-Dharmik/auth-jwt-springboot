package com.neoutilix.auth_jwt_springboot.repository;

import com.neoutilix.auth_jwt_springboot.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
