package com.example.test2;

public class Section {
    private String sectionName;

    public Section() {
        // Default constructor required for Firestore serialization
    }

    public Section(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}

