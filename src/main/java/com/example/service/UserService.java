package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsernameandPassword(String username,String password) {
        return userRepository.findByUsernameAndPassword(username,password);
    }
    public User save(User user) {
    	if((userRepository.findByUsername(user.getUsername()))!=null) {
    		throw new RuntimeException("Username already exists");
    	}
    
        if (user.getUsername().length() < 3) {
            throw new IllegalArgumentException("Username must be exactly 8 characters long.");
        }

        String password = user.getPassword();
        if (password.length() < 8 || !password.matches("(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&]).*")) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and include letters, numbers, and symbols.");
        }
        
        return userRepository.save(user);
    }
    public User authenticate(String username, String password) {
        User user = findByUsernameandPassword(username,password);
        return (user != null && user.getPassword().equals(password)) ? user : null;
    }
    public void updateCollegeCredentialsByAdmin(int collegeId, String newUsername, String newPassword) {
        if (newUsername.length() < 3) {
            throw new IllegalArgumentException("Username must be exactly 8 characters long.");
        }

        if (newPassword.length() < 8 || !newPassword.matches("(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&]).*")) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and include letters, numbers, and symbols.");
        }

        Optional<User> userOptional = userRepository.findByRoleSpecificIdAndRole(collegeId, "college");
        if (userOptional.isPresent()) {
            User collegeUser = userOptional.get();
            collegeUser.setUsername(newUsername);
            collegeUser.setPassword(newPassword);
            userRepository.save(collegeUser);
        } else {
            throw new IllegalArgumentException("College not found with ID: " + collegeId);
        }
    }
    public User getCollegeUserByCollegeId(int collegeId) {
        Optional<User> userOptional = userRepository.findByRoleSpecificIdAndRole(collegeId, "college");

        if (userOptional.isPresent()) {
            return userOptional.get(); // Return the college user
        } else {
            throw new IllegalArgumentException("College not found with ID: " + collegeId);
        }
    }
    public void deleteCollegeUserByCollegeId(int collegeId) {
        Optional<User> userOptional = userRepository.findByRoleSpecificIdAndRole(collegeId, "college");
        
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
        } else {
            throw new IllegalArgumentException("No user found with college ID: " + collegeId);
        }
    }
    public void deleteStudentUserByStudentId(int studentId) {
        Optional<User> userOptional = userRepository.findByRoleSpecificIdAndRole(studentId, "student");
        
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
        } else {
            throw new IllegalArgumentException("No user found with college ID: " + studentId);
        }
    }
    public void updateStudentPassword(int studentId, String newPassword) {
        Optional<User> userOptional = userRepository.findByRoleSpecificIdAndRole(studentId, "student");
        
        if (userOptional.isPresent()) {
            User studentUser = userOptional.get();
            studentUser.setPassword(newPassword);
            userRepository.save(studentUser);
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }
    }
    public void updateCollegePassword(int collegeId, String newPassword) {
        Optional<User> userOptional = userRepository.findByRoleSpecificIdAndRole(collegeId, "college");
        
        if (userOptional.isPresent()) {
            User collegeUser = userOptional.get();
            collegeUser.setPassword(newPassword);
            userRepository.save(collegeUser);
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + collegeId);
        }
    }
    public void updateStudentProfilePhoto(int studentId, String photoPath) {
        Optional<User> userOptional = userRepository.findByRoleSpecificIdAndRole(studentId, "student");
    
        if (userOptional.isPresent()) {
            User studentUser = userOptional.get();
            studentUser.setProfilePhoto(photoPath);
            userRepository.save(studentUser);
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }
    }
    public void updateCollegeProfilePhoto(int collegeId, String photoPath) {
        Optional<User> userOptional = userRepository.findByRoleSpecificIdAndRole(collegeId, "college");
    
        if (userOptional.isPresent()) {
            User studentUser = userOptional.get();
            studentUser.setProfilePhoto(photoPath);
            userRepository.save(studentUser);
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + collegeId);
        }
    }
    public String getStudentProfilePhotoPath(int studentId) {
        Optional<User> userOptional = userRepository.findByRoleSpecificIdAndRole(studentId, "student");
    
        if (userOptional.isPresent()) {
            return userOptional.get().getProfilePhoto(); // Returns stored path of profile photo
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }
    }
    public String getCollegeProfilePhotoPath(int collegeId) {
        Optional<User> userOptional = userRepository.findByRoleSpecificIdAndRole(collegeId, "college");
    
        if (userOptional.isPresent()) {
            return userOptional.get().getProfilePhoto(); // Returns stored path of profile photo
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + collegeId);
        }
    }
    



}
