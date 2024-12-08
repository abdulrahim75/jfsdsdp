package com.example.controller;

import com.example.entity.Achievement;
import com.example.entity.Student;
import com.example.entity.User;
import com.example.service.AchievementService;
import com.example.service.StudentService;
import com.example.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colleges")
@CrossOrigin
public class CollegeController {
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private AchievementService achievementService;
    
    @Autowired
    private UserService userService;

    @PostMapping("/{collegeId}/students")
    public Student addStudent(@PathVariable Long collegeId, @RequestBody Student student) {
    	 return studentService.save(collegeId, student);
    }
    
    @PostMapping("/addstudentuser")
    public User addCollegeUser(@RequestBody User user) {
    	  return userService.save(user);
    }
   

    @GetMapping("/{collegeId}/students")
    public List<Student> getStudents(@PathVariable Long collegeId) {
        return studentService.findByCollegeId(collegeId); // Modify to return students for specific college
    }
    @PutMapping("/students/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    // Delete Student
    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable Long id) {
    	List<Achievement> achievements = achievementService.findByStudentId(id);
        for (Achievement achievement : achievements) {
            achievementService.delete(achievement.getId());
        }
    	System.out.println("deleted Student");
        studentService.delete(id);
    }
    @DeleteMapping("/studentsuser/{id}")
    public void deleteCollegeUser(@PathVariable Long id) {
    	System.out.println("Deletes Student User");
    	userService.deleteStudentUserByStudentId(id.intValue());
    	
    }
    @PostMapping("/students/{rollNumber}/achievements/add")
    public Achievement addAchievement(@PathVariable String rollNumber, @RequestBody Achievement achievement) {
    	 Student student = studentService.findByRollNumber(rollNumber);
         if (student != null) {
             achievement.setStudent(student);
             return achievementService.save(achievement);
         } else {
             throw new RuntimeException("Student not found with roll number: " + rollNumber);
         }
    }

    // Get list of achievements for a student by roll number
    @GetMapping("/students/{rollNumber}/achievements")
    public List<Achievement> getAchievementsByStudentRollNumber(@PathVariable String rollNumber) {
        Student student = studentService.findByRollNumber(rollNumber);
        if (student != null) {
            return achievementService.findByStudentId(student.getId());
        } else {
            throw new RuntimeException("Student not found with roll number: " + rollNumber);
        }
    }

    // Update Achievement
    @PutMapping("/achievements/update/{id}")
    public Achievement updateAchievement(@PathVariable Long id, @RequestBody Achievement achievement) {
        return achievementService.update(id, achievement);
    }

    // Delete Achievement
    @DeleteMapping("/achievements/delete/{id}")
    public void deleteAchievement(@PathVariable Long id) {
        achievementService.delete(id);
    }

}
