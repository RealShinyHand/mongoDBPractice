package com.skj.mongopractice.service;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.result.UpdateResult;
import com.skj.mongopractice.domain.Professor;
import com.skj.mongopractice.domain.Student;
import com.skj.mongopractice.dto.TempResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class StudentServiceImpl implements StudentService{

    private  final MongoTemplate mongoTemplate;

    @Override
    public void updateAdvisor(int grade, String pName) {

        Query query = new Query();
        query.addCriteria(Criteria.where("pName").is(pName));
        Professor professor =  mongoTemplate.findOne(query, Professor.class);

        query = new Query();
        query.addCriteria(Criteria.where("grade").is(grade));
        Update update = Update.update("advisorProfessor",professor);
        //getId해도 되고 인스턴스 자체로 해도되고 다 되네 ...
        UpdateResult updateResult = mongoTemplate.updateMulti(query,update,Student.class);
        log.info("update Result : {}",updateResult);
    }

    @Override
    public List<Student> findByAdvisorStudent(String pName){
        Query query = new Query();
        query.addCriteria(Criteria.where("pName").is(pName));
        Professor professor =  mongoTemplate.findOne(query, Professor.class);

        query = new Query();
        query.addCriteria(Criteria.where("advisorProfessor").is(professor.getId()));
        return  mongoTemplate.find(query,Student.class);
    }

    @Override
    public List<TempResultDto> findAllProfessorWithAdviseStudentCount() {
        Query query = new Query();
        String from= "students";
        String localField = "id";
        String foreignField = "advisorProfessor";
        String asField ="ad";
        LookupOperation lo= Aggregation.lookup(from,localField,foreignField,asField);
        GroupOperation go = Aggregation.group("advisorProfessor").count().as("count");
        SortOperation so = Aggregation.sort(Sort.Direction.ASC,"count");
        Aggregation aggregation = Aggregation.newAggregation(lo,go,so);

        List<TempResultDto> list= mongoTemplate.aggregate(aggregation,Student.class, TempResultDto.class).getMappedResults();

        //mongoTemplate.ag
        return list;
    }

    @Override
    public List<Student> findAll() {

        return mongoTemplate.findAll(Student.class);
    }

    @Override
    public int getTotalSalary() {


       Aggregation aggregation = Aggregation.newAggregation(Aggregation.group().sum("salary").as("totalSalary"));

       AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation,Professor.class, HashMap.class);
       log.info("mappedResult : {}",aggregationResults.getMappedResults());
        log.info("RawResults : {}",aggregationResults.getRawResults());
        log.info("ServerUsed : {}",aggregationResults.getServerUsed());
        return ( int )aggregationResults.getMappedResults().get(0).get("totalSalary");
    }

    @Override
    public void addProfessorSalary(String pId, int addedSalary) {

        //Query query = new Query();
        //query.addCriteria(Criteria.where("pId").is(pId));
        //Professor professor =mongoTemplate.findOne(query,Professor.class);


        //professor.setSalary(professor.getSalary() + addedSalary);
        //몽고 db에서는 기존  필드를 참조해서 업데이트하는 것이 안된다....
        //http://time2relax.net/wp/?p=972
        //mongoTemplate.save(professor);

        //몽고 db가 writeConflict를 알아서 처리하긴 하나 , 각각의 트랜잭션(조회, 조회값을 통한 업데이트) 임으로
        //몇몇 오퍼가 에러남
        //====================================위 코드는 동시성 제어가 되어 있지 않다.
        ClientSession session = ClientSessionOptions.builder()
                .
        mongoTemplate.set

    }

}
