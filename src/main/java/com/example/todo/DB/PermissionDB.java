package com.example.springdb.DB;

import com.example.springdb.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionDB extends JpaRepository<Permission, Integer> {
}
