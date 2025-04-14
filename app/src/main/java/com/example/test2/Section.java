package com.example.test2;

public class Section {
    private String sectionName;
    private String gradeLevel;

    public Section() {
        // Default constructor required for Firestore serialization
    }

    public Section(String sectionName, String gradeLevel) {
        this.sectionName = sectionName;
        this.gradeLevel = gradeLevel;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }
}
