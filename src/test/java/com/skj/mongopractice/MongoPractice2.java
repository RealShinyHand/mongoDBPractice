package com.skj.mongopractice;

import com.skj.mongopractice.domain.Professor;
import com.skj.mongopractice.domain.Student;
import com.skj.mongopractice.domain.Subject;
import com.skj.mongopractice.dto.TempResultDto;
import com.skj.mongopractice.service.StudentService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Professor,Student,Subject 를 통한 연습
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class MongoPractice2 {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    StudentService studentService;


    @Order(1)
    @Test
    void initialize() {
        // 학생 생성
        mongoTemplate.dropCollection(Student.class);
        generateBasicFieldStudent("000001", 1, "일길일");
        generateBasicFieldStudent("000002", 1, "홍길동");
        generateBasicFieldStudent("000003", 1, "박수현");
        generateBasicFieldStudent("000004", 2, "고길동");
        generateBasicFieldStudent("000005", 2, "신빛나");
        generateBasicFieldStudent("000006", 2, "이총명");
        generateBasicFieldStudent("000007", 2, "박특출");
        generateBasicFieldStudent("000008", 4, "김평범");

        mongoTemplate.dropCollection(Professor.class);
        generateBasicFieldProfessor("001", "나교수", 1000);
        generateBasicFieldProfessor("002", "폰꺼이만", 2000);
        generateBasicFieldProfessor("003", "컴터튜닝", 3000);
        generateBasicFieldProfessor("004", "황의법칙", 4000);

        mongoTemplate.dropCollection(Subject.class);
        generateBasicFieldSubject("aaaaa", "컴퓨터 구조", 3);
        generateBasicFieldSubject("aaaab", "컴퓨터 시스템 및 어셈블리어", 2);
        generateBasicFieldSubject("aaaac", "기업 회계와 이해", 3);
        generateBasicFieldSubject("aaaad", "공학 윤리", 1);
        generateBasicFieldSubject("aaaae", "소프트웨어 공학", 3);
        generateBasicFieldSubject("aaaaf", "클라우드 컴퓨팅", 3);
        generateBasicFieldSubject("aaaag", "성과 사회", 2);
        generateBasicFieldSubject("aaaah", "Practical English", 2);


    }

    @Test
    @Order(2)
    @DisplayName("1. 관계 맺기 : 지도교수 부여하기 시나리오")
    void givenPNameAndGrade_whenAdvisorUpdate_thenSuccess() {
        //given
        int grade = 1;
        String pName = "나교수";
        //when
        //1.학년(grade) == grade인 학생에게 pName에 일치하는 지도교수를 부여하세요.
        studentService.updateAdvisor(grade, pName);
        //2.pName이 일치하는 교수의 지도학생들 리스트를 반환하시오.
        List<Student> studentList = studentService.findByAdvisorStudent(pName);
        Assertions.assertEquals(3, studentList.size());

        studentService.updateAdvisor(2, "폰꺼이만");
        studentService.updateAdvisor(3, "컴터튜닝");
        studentService.updateAdvisor(4, "황의법칙");
    }

    @Test
    @Order(3)
    @DisplayName("2. group count : 지도 교수가 몇명의 학생을 지도하는 지 출력,")
    void givenNoting_when_then() {
        //단순히 Aggregation 사용을 위한 Test Case 입니다.
        //프로페서,스튜던트를 lookup을 사용해 조인시키고, 프로페서 id를 통해 grouping하여 count를 구하고, count 순으로 정렬하여 반환하세요
        List<TempResultDto> professorDtoList = (List<TempResultDto>) studentService.findAllProfessorWithAdviseStudentCount();

        Assertions.assertEquals(3, professorDtoList.size());

        Assertions.assertEquals(1, professorDtoList.get(0).getCount());

        Assertions.assertEquals(3, professorDtoList.get(1).getCount());

        Assertions.assertEquals(4, professorDtoList.get(2).getCount());

        System.out.println(professorDtoList);
    }

    @Test
    @Order(4)
    @DisplayName("3.lazyLoding 을 적용해보세요. Student Class의 advisorProfessor필드의 @DocumentRef(lazy= true)로 변경")
    void given_when_thenReturnStudentList() {
        List<Student> studentList = studentService.findAll();
        Assertions.assertEquals(8, studentList.size());
        Assertions.assertNotEquals(Professor.class.getName(), studentList.get(0).getAdvisorProfessor().getClass().getName());
    }

    @Test
    @Order(5)
    @DisplayName("4.단일 컬렉션 특정 필드 sum  연습: 교수들의 총 salary 를 반환하세요.")
    void given_when_thenReturnTotalSalary() {
        int totalSalary = studentService.getTotalSalary();
        Assertions.assertEquals(10000, totalSalary);
    }

    //======================

    @Test
    @Order(6)
    @DisplayName("5. 동시성 테스트 : writeConflict 를 이해해볼까?")
    void givenPidAndaddedSalary_whenUpdateSalary_thenTargetSalary2000() throws ExecutionException, InterruptedException {
        //mongodb는 같은 도큐먼트 내용을 수정할 떄 , 알아서 잠금(snap-shot)을 걸고 다른 트랜잭션이 쓰기 접근을 하면 발생하는
        //writeConflict를 알아서 처리한다.
        // 나교수의 salary를 10원 추가하라..
        Query query = new Query();
        query.addCriteria(Criteria.where("pId").is("001"));

        Professor professor = mongoTemplate.findOne(query,Professor.class);
        professor.setSalary(1000);
        mongoTemplate.save(professor);


        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Future<String>> futureList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Future<String> future = executorService.submit(() -> {
                studentService.addProfessorSalary("001", 10);
                return "COMPLETE";
            });
            futureList.add(future);
        }

        for(Future<String> future : futureList){
            future.get();
        }

        Professor savedProfessor = mongoTemplate.findOne(query,Professor.class);
        Assertions.assertEquals(2000,savedProfessor.getSalary());

    }

    private void generateBasicFieldSubject(String id, String name, int point) {
        Subject subject = new Subject();
        subject.setSName(name);
        subject.setSId(id);
        subject.setPoint(point);
        mongoTemplate.insert(subject);
    }

    private void generateBasicFieldProfessor(String id, String name, int salary) {
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
