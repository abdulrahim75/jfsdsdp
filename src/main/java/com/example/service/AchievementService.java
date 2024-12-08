package com.example.service;

import com.example.entity.Achievement;

import com.example.repository.AchievementRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementService {
    @Autowired
    private AchievementRepository achievementRepository;
    

    public List<Achievement> findAll() {
        return achievementRepository.findAll();
    }

    public Achievement save(Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    public Achievement findById(Long id) {
        return achievementRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        achievementRepository.deleteById(id);
    }
    public List<Achievement> findByStudentId(Long studentId) {
        return achievementRepository.findByStudentId(studentId);
    }
    public void delete(Long id) {
        achievementRepository.deleteById(id);
    }
    public Achievement update(Long id, Achievement achievement) {
        Achievement existingAchievement = achievementRepository.findById(id).orElse(null);
        if (existingAchievement != null) {
            // Update only non-null fields
            if (achievement.getActivityName() != null) {
                existingAchievement.setActivityName(achievement.getActivityName());
            }
            if (achievement.getActivityDescription() != null) {
                existingAchievement.setActivityDescription(achievement.getActivityDescription());
            }
            if (achievement.getActivityCategory() != null) {
                existingAchievement.setActivityCategory(achievement.getActivityCategory());
            }
            if (achievement.getActivityDate() != null) {
                existingAchievement.setActivityDate(achievement.getActivityDate());
            }
            if(achievement.getActivitypoints() != 0) {
            	existingAchievement.setActivitypoints(achievement.getActivitypoints());
            }
            
            existingAchievement.setFirstPosition(achievement.isFirstPosition());
            existingAchievement.setSecondPosition(achievement.isSecondPosition());
            existingAchievement.setThirdPosition(achievement.isThirdPosition());
            existingAchievement.setParticipation(achievement.isParticipation());

            return achievementRepository.save(existingAchievement);
        }
        return null; // Handle the case where the achievement doesn't exist
    }
    
    
    
}
