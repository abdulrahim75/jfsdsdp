package com.example.entity;



import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String activityName;
    private String activityDescription;
    private String activityCategory;
    @Temporal(TemporalType.DATE)
    private Date activityDate;
    private int activitypoints;
  
	private boolean firstPosition;
    private boolean secondPosition;
    private boolean thirdPosition;
    private boolean participation;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getActivityDescription() {
		return activityDescription;
	}
	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}
	public String getActivityCategory() {
		return activityCategory;
	}
	public void setActivityCategory(String activityCategory) {
		this.activityCategory = activityCategory;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public boolean isFirstPosition() {
		return firstPosition;
	}
	public void setFirstPosition(boolean firstPosition) {
		this.firstPosition = firstPosition;
	}
	public boolean isSecondPosition() {
		return secondPosition;
	}
	public void setSecondPosition(boolean secondPosition) {
		this.secondPosition = secondPosition;
	}
	public boolean isThirdPosition() {
		return thirdPosition;
	}
	public void setThirdPosition(boolean thirdPosition) {
		this.thirdPosition = thirdPosition;
	}
	public boolean isParticipation() {
		return participation;
	}
	public void setParticipation(boolean participation) {
		this.participation = participation;
	}
	public Date getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}
	public int getActivitypoints() {
		return activitypoints;
	}
	public void setActivitypoints(int activitypoints) {
		this.activitypoints = activitypoints;
	}
		

    
}
