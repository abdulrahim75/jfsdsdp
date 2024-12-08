package com.example.service;


import com.example.entity.College;
import com.example.entity.Student;
import com.example.repository.CollegeRepository;
import com.example.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CollegeRepository collegeRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student save(Long collegeId,Student student) {
    	  if (studentRepository.findByRollNumber(student.getRollNumber()) != null) {
              throw new RuntimeException("Roll Number already exists");
          }
          if (studentRepository.findByEmail(student.getEmail()) != null) {
              throw new RuntimeException("Email already exists");
          }
          if (studentRepository.findByPhoneNumber(student.getPhoneNumber()) != null) {
              throw new RuntimeException("Phone Number already exists");
          }
    	College college = collegeRepository.findById(collegeId).orElseThrow(() -> new RuntimeException("College not found"));
        student.setCollege(college);
        return studentRepository.save(student);
    }
    

    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
    public List<Student> findByCollegeId(Long collegeId) {
        return studentRepository.findByCollegeId(collegeId);
    }
    public Student update(Long id, Student student) {
        Student existingStudent = studentRepository.findById(id).orElse(null);
        if (existingStudent != null) {
            // Update only non-null fields
            if (student.getName() != null) {
                existingStudent.setName(student.getName());
            }
            if (student.getRollNumber() != null) {
                existingStudent.setRollNumber(student.getRollNumber());
            }
            if (student.getDob() != null) {
                existingStudent.setDob(student.getDob());
            }
            if (student.getEmail() != null) {
                existingStudent.setEmail(student.getEmail());
            }
            if (student.getPhoneNumber() != null) {
                existingStudent.setPhoneNumber(student.getPhoneNumber());
            }
            if (student.getBranch() != null) {
                existingStudent.setBranch(student.getBranch());
            }
            if (student.getDegree() != null) {
                existingStudent.setDegree(student.getDegree());
            }
            if (student.getDegreeYear() != null) {
                existingStudent.setDegreeYear(student.getDegreeYear());
            }
            if (student.getBio() != null) {
                existingStudent.setBio(student.getBio());
            }

            return studentRepository.save(existingStudent);
        }
        return null; // Handle the case where the student doesn't exist
    }
    public Student findByRollNumber(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber);
    }



}
