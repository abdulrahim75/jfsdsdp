package com.example.repository;
import com.example.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College, Long> {
	College findByEmail(String email);
}
