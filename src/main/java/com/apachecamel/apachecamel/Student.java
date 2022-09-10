package com.apachecamel.apachecamel;

public class Student {

    private int id;
    private String name;
    private String studentNumber;

    public Student() {
    }

    public Student(int id, String name, String studentNumber) {
        this.id = id;
        this.name = name;
        this.studentNumber = studentNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}
