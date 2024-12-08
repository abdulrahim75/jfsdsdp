package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import com.example.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private static final String STUDENT_IMAGE_DIRECTORY = System.getProperty("user.dir") + "/images/studentprofile/";
    private static final String COLLEGE_IMAGE_DIRECTORY = System.getProperty("user.dir") + "/images/collegeprofile/";

    // Authentication and Token Management
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginData) {
        User authenticatedUser = userService.authenticate(loginData.getUsername(), loginData.getPassword());

        if (authenticatedUser != null) {
            String token = jwtUtil.generateToken(
                authenticatedUser.getUsername(),
                String.valueOf(authenticatedUser.getRole()),
                String.valueOf(authenticatedUser.getRoleSpecificId())
            );

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", authenticatedUser.getUsername());
            response.put("role", authenticatedUser.getRole());
            response.put("roleSpecificId", authenticatedUser.getRoleSpecificId());

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.getClaims(token);
                    Map<String, Object> response = new HashMap<>();
                    response.put("valid", true);
                    response.put("username", claims.getSubject());
                    response.put("role", claims.get("role"));
                    response.put("roleSpecificId", claims.get("roleSpecificId"));
                    return ResponseEntity.ok(response);
                }
            } catch (ExpiredJwtException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
            } catch (MalformedJwtException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token format");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token validation error: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody Map<String, String> credentials) {
        User user = userService.findByUsernameandPassword(credentials.get("username"), credentials.get("password"));
        return ResponseEntity.ok(user != null);
    }

    // Password Management
    @PutMapping("/update-student-password/{studentId}")
    public ResponseEntity<?> updateStudentPassword(@PathVariable int studentId, @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        try {
            userService.updateStudentPassword(studentId, newPassword);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update-college-password/{collegeId}")
    public ResponseEntity<?> updateCollegePassword(@PathVariable int collegeId, @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        try {
            userService.updateCollegePassword(collegeId, newPassword);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Profile Photo Management
    @PostMapping("/update-student-profile-photo/{studentId}")
    public ResponseEntity<?> updateStudentProfilePhoto(@PathVariable int studentId, @RequestParam("photo") MultipartFile photo, @RequestParam("username") String username) {
        return handleProfilePhotoUpload(studentId, username, photo, STUDENT_IMAGE_DIRECTORY, true);
    }

    @PostMapping("/update-college-profile-photo/{collegeId}")
    public ResponseEntity<?> updateCollegeProfilePhoto(@PathVariable int collegeId, @RequestParam("photo") MultipartFile photo, @RequestParam("username") String username) {
        return handleProfilePhotoUpload(collegeId, username, photo, COLLEGE_IMAGE_DIRECTORY, false);
    }

    private ResponseEntity<?> handleProfilePhotoUpload(int id, String username, MultipartFile photo, String directory, boolean isStudent) {
        makeDirectoryIfNotExist(directory);

        String fileName = username + ".jpg";
        Path filePath = Paths.get(directory, fileName);

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("Existing photo deleted: " + fileName);
            }
            Files.write(filePath, photo.getBytes());

            if (isStudent) {
                userService.updateStudentProfilePhoto(id, filePath.toString());
            } else {
                userService.updateCollegeProfilePhoto(id, filePath.toString());
            }

            System.out.println("Uploaded Photo");
            return new ResponseEntity<>(fileName, HttpStatus.CREATED);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image not uploaded: " + ex.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void makeDirectoryIfNotExist(String imageDirectory) {
        File directory = new File(imageDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // Retrieve Profile Photo
    @GetMapping("/get-student-profile-photo/{studentId}")
    public ResponseEntity<?> getStudentProfilePhoto(@PathVariable int studentId) {
        return getProfilePhoto(studentId, true);
    }

    @GetMapping("/get-college-profile-photo/{collegeId}")
    public ResponseEntity<?> getCollegeProfilePhoto(@PathVariable int collegeId) {
        return getProfilePhoto(collegeId, false);
    }

    private ResponseEntity<?> getProfilePhoto(int id, boolean isStudent) {
        try {
            String photoPath = isStudent ? userService.getStudentProfilePhotoPath(id) : userService.getCollegeProfilePhotoPath(id);
            File photoFile = new File(photoPath);

            if (!photoFile.exists()) {
                return ResponseEntity.notFound().build();
            }

            byte[] photoBytes = Files.readAllBytes(photoFile.toPath());
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photoBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading photo: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
