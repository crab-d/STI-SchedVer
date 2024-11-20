package com.example.test2;

public class Student {
    private String name, email, studentNumber, section, gender;

    public Student(String name, String email, String studentNumber, String section, String gender) {
        this.name = name;
        this.email = email;
        this.studentNumber = studentNumber;
        this.section = section;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getSection() {
        return section;
    }

    public String getGender() {
        return gender;
    }
}
