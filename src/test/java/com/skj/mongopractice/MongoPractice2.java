package com.skj.mongopractice;

import com.skj.mongopractice.domain.Professor;
import com.skj.mongopractice.domain.Student;
import com.skj.mongopractice.domain.Subject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * Professor,Student,Subject 를 통한 연습
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class MongoPractice2 {

    @Autowired
    MongoTemplate mongoTemplate;


    @Order(1)
    @Test
    void initialize(){
        // 학생 생성
        mongoTemplate.dropCollection(Student.class);
        generateBasicFieldStudent("000001",1,"일길일");
        generateBasicFieldStudent("000002",1,"홍길동");
        generateBasicFieldStudent("000003",1,"박수현");
        generateBasicFieldStudent("000004",2,"고길동");
        generateBasicFieldStudent("000005",2,"신빛나");
        generateBasicFieldStudent("000006",2,"이총명");
        generateBasicFieldStudent("000007",2,"박특출");
        generateBasicFieldStudent("000008",4,"김평범");

        mongoTemplate.dropCollection(Professor.class);
        generateBasicFieldProfessor("001","나교수",1000);
        generateBasicFieldProfessor("001","폰꺼이만",1000);
        generateBasicFieldProfessor("001","컴터튜닝",1000);
        generateBasicFieldProfessor("001","황의법칙",1000);

        mongoTemplate.dropCollection(Subject.class);
        generateBasicFieldSubject("aaaaa","컴퓨터 구조",3);
        generateBasicFieldSubject("aaaab","컴퓨터 시스템 및 어셈블리어",2);
        generateBasicFieldSubject("aaaac","기업 회계와 이해",3);
        generateBasicFieldSubject("aaaad","공학 윤리",1);
        generateBasicFieldSubject("aaaae","소프트웨어 공학",3);
        generateBasicFieldSubject("aaaaf","클라우드 컴퓨팅",3);
        generateBasicFieldSubject("aaaag","성과 사회",2);
        generateBasicFieldSubject("aaaah","Practical English",2);


    }

    private  void generateBasicFieldSubject(String id,String name,int point){
        Subject subject = new Subject();
        subject.setSName(name);
        subject.setSId(id);
        subject.setPoint(point);
        mongoTemplate.insert(subject);
    }
    private void generateBasicFieldProfessor(String id, String name , int salary) {
        Professor professor = new Professor();
        professor.setPId(id);
        professor.setPName(name);
        professor.setSalary(salary);
        mongoTemplate.insert(professor);
    }

    private void generateBasicFieldStudent(String s, int i, String name) {
        Student student = new Student();
        student.setStudentId(s);
        student.setGrade(i);
        student.setSName(name);
        mongoTemplate.insert(student);
    }


}
