package com.example.controller;

import com.example.entity.College;
import com.example.entity.Student;
import com.example.entity.User;
import com.example.entity.Achievement;
import com.example.service.CollegeService;
import com.example.service.StudentService;
import com.example.service.UserService;
import com.example.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private CollegeService collegeService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private AchievementService achievementService;
    @Autowired
    private UserService userService;

    @GetMapping("/fetchstats")
    public Map<String, Long> fetchStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("colleges", (long) collegeService.findAll().size());
        stats.put("students", (long) studentService.findAll().size());
        stats.put("achievements", (long) achievementService.findAll().size());
        return stats;
    }

    @PostMapping("/colleges")
    public College addCollege(@RequestBody College college) {
        return collegeService.save(college);
    }

    @PostMapping("/addcollegeuser")
    public User addCollegeUser(@RequestBody User user) {
        System.out.println("Admin " + user);
        return userService.save(user);
    }

    @GetMapping("/colleges")
    public List<College> getColleges() {
        return collegeService.findAll();
    }

    @GetMapping("/colleges/{id}")
    public College getCollegeById(@PathVariable Long id) {
        return collegeService.findById(id);
    }

    @GetMapping("/college-user/{collegeId}")
    public User getCollegeUserByCollegeId(@PathVariable int collegeId) {
        return userService.getCollegeUserByCollegeId(collegeId);
    }

    @GetMapping("/students")
    public List<Student> getStudents() {
        return studentService.findAll();
    }

    @GetMapping("/achievements")
    public List<Achievement> getAllAchievements() {
        return achievementService.findAll();
    }

    @PutMapping("/colleges/{id}")
    public College updateCollege(@PathVariable Long id, @RequestBody College college) {
        return collegeService.update(id, college);
    }

    @PutMapping("/update-college-credentials/{collegeId}")
    public String updateCollegeCredentials(@PathVariable int collegeId,
                                           @RequestParam String newUsername,
                                           @RequestParam String newPassword) {
        userService.updateCollegeCredentialsByAdmin(collegeId, newUsername, newPassword);
        return "College credentials updated successfully";
    }

    @DeleteMapping("/colleges/{id}")
    public void deleteCollege(@PathVariable Long id) {
        System.out.println("Deleted College and Students and Achievements");
        List<Student> students = studentService.findByCollegeId(id);
        for (Student student : students) {
            List<Achievement> achievements = achievementService.findByStudentId(student.getId());
            for (Achievement achievement : achievements) {
                achievementService.delete(achievement.getId());
            }
            studentService.delete(student.getId());
        }
        collegeService.delete(id);
    }

    @DeleteMapping("/collegesuser/{id}")
    public void deleteCollegeUser(@PathVariable Long id) {
        System.out.println("Deleted College User and Student User");
        List<Student> students = studentService.findByCollegeId(id);
        for (Student student : students) {
            userService.deleteStudentUserByStudentId(student.getId().intValue());
        }
        userService.deleteCollegeUserByCollegeId(id.intValue());
    }
}
