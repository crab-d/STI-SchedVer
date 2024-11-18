package com.example.test2;

public class Schedule {
    private String subject;
    private String time;
    private String room;
    private String professor;
    private String day;

    // Constructor
    public Schedule(String day, String subject, String time, String room, String professor) {
        this.day = day;
        this.subject = subject;
        this.time = time;
        this.room = room;
        this.professor = professor;
    }

    // Getters and setters
    public String getSubject() {
        return subject;
    }

    public String getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }

    public String getProfessor() {
        return professor;
    }

    public String getDay() {
        return day;
    }
}


