package com.ganpatispringsecurity.springsecurity.repository;

import com.ganpatispringsecurity.springsecurity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {
}
