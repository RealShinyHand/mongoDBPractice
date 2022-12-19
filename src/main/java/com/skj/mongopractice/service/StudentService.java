package com.skj.mongopractice.service;

import com.skj.mongopractice.domain.Student;
import com.skj.mongopractice.dto.TempResultDto;

import java.util.List;

public interface StudentService {
    void updateAdvisor(int grade, String pName);
    public List<Student> findByAdvisorStudent(String pName);

    List<TempResultDto> findAllProfessorWithAdviseStudentCount();

    List<Student> findAll();

    int getTotalSalary();

    void addProfessorSalary(String s, int i);
}
