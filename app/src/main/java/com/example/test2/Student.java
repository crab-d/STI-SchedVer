package com.example.test2;

public class Student {
    private String Lname, Fname, name, email, studentNumber, section, gender;

    public Student(String Lname, String Fname, String name, String email, String studentNumber, String section, String gender) {
        this.name = name;
        this.email = email;
        this.studentNumber = studentNumber;
        this.section = section;
        this.gender = gender;
        this.Lname = Lname;
        this.Fname = Fname;
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
    
    public String getLname() {
        return Lname;
    }
    
    public String getFname() {
        return Fname;
    }
    
}
