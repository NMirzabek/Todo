package com.example.springdb.DB;

import com.example.springdb.entity.Album;
import com.example.springdb.entity.User;
import com.example.springdb.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserData extends JpaRepository<User, Integer> {
    @Query(value = """
SELECT
    u.username AS username,
    COUNT(DISTINCT a.id) AS albumCount,
    COUNT(DISTINCT p.id) AS postCount,
    COUNT(DISTINCT td.id) AS todoCount
FROM users u
         LEFT JOIN album a ON u.id = a.user_id
         LEFT JOIN post p ON u.id = p.user_id
         LEFT JOIN to_dos td ON u.id = td.user_id
GROUP BY u.username
""", nativeQuery = true)
    List<UserReport> getReport();

}
