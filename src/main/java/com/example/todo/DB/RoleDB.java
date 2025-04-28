package com.example.springdb.DB;

import com.example.springdb.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDB extends JpaRepository<Role, Integer> {
}
