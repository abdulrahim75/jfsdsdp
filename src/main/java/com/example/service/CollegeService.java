package com.example.service;


import com.example.entity.College;
import com.example.repository.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeService {
    @Autowired
    private CollegeRepository collegeRepository;
     
    public long getAllColleges(){
    	return collegeRepository.count();
    }

    public List<College> findAll() {
        return collegeRepository.findAll();
    }

    public College save(College college) {
    	if (collegeRepository.findByEmail(college.getEmail()) != null) {
            throw new RuntimeException("Roll Number already exists");
        }
        College singlecollege = collegeRepository.save(college);
        System.out.println(singlecollege.toString());
        return singlecollege;
    }
    

    public College findById(Long id) {
        return collegeRepository.findById(id).orElse(null);
    }
 

    public void deleteById(Long id) {
        collegeRepository.deleteById(id);
    }
    
    public College update(Long id, College college) {
    	if (collegeRepository.findByEmail(college.getEmail()) != null) {
            throw new RuntimeException("Roll Number already exists");
        }
        College existingCollege = collegeRepository.findById(id).orElse(null);
        if (existingCollege != null) {
            existingCollege.setName(college.getName());
            existingCollege.setLocation(college.getLocation());
            existingCollege.setEmail(college.getEmail());
            return collegeRepository.save(existingCollege);
        }
        return null;
    }
    public void delete(Long id) {
        collegeRepository.deleteById(id);
    }
}
