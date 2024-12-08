package com.example.repository;

import com.example.entity.Achievement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
	 List<Achievement> findByStudentId(Long studentId);
}
