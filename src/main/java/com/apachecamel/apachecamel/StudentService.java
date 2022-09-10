package com.apachecamel.apachecamel;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private List<Student> list = new ArrayList<>();

    @PostConstruct
    public void initDB(){
        list.add(new Student(1,"Mobile","5000"));
        list.add(new Student(2,"Book","400"));
    }

    public Student addStudent(Student student){
        list.add(student);
        return student;
    }

    public List<Student> getStudents(){
        return list;
    }

}
