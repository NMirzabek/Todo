package com.example.springdb.DB;
import com.example.springdb.entity.JsonUser;
import com.example.springdb.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JsonUserDB extends JpaRepository<JsonUser, Integer>{
    Optional<JsonUser> findByUsername(String username);
        List<JsonUser> findByIdNot(Integer id);
}
