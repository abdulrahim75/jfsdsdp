package com.example.repository;



import com.example.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username,String password);
    Optional<User> findByRoleSpecificIdAndRole(int roleSpecificId, String role);
    User findByUsername(String username);
}
