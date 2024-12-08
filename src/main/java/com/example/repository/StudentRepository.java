package com.example.repository;
import com.example.entity.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
	List<Student> findByCollegeId(Long collegeId);
	Student findByRollNumber(String rollnumber);
	Student findByEmail(String email);
	Student findByPhoneNumber(String phoneNumber);
}
