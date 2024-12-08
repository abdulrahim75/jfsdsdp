package com.example.controller;

import com.example.entity.Achievement;
import com.example.entity.Student;
import com.example.service.AchievementService;
import com.example.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {
	@Autowired
    private AchievementService achievementService;

	@Autowired
	private StudentService studentService;

    // Get Achievements by Student
	@GetMapping("/{studentId}/achievements")
	public List<Achievement> getAchievementsByStudent(@PathVariable Long studentId) {
	    System.out.println("Fetching achievements for student ID: " + studentId);
	    List<Achievement> achievements = achievementService.findByStudentId(studentId);
	    System.out.println("Achievements found: " + achievements);
	    return achievements;
	}

	@PutMapping("/{id}")
	public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
		return studentService.update(id, student);
	}

	@GetMapping("/{id}")
	public Student getStudent(@PathVariable Long id) {
		return studentService.findById(id);
	}

}
