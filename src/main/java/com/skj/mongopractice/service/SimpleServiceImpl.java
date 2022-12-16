package com.skj.mongopractice.service;

import com.skj.mongopractice.domain.Simple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleServiceImpl {
    private final MongoTemplate mongoTemplate;

    public void insertOne(Simple simple){
        Simple inserted = mongoTemplate.insert(simple);
    }

    public void deleteAll() {
        mongoTemplate.remove(Simple.class).all();
    }

    public List<Simple> findAll() {
        return mongoTemplate.findAll(Simple.class);
    }

    public Simple findByTitle(String title){

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(title));
        return mongoTemplate.findOne(query,Simple.class);
    }

    public List<Simple> findByLikeTitle(String title){
        Query query = new Query();
        query.addCriteria(Criteria.where("title").regex("^.*" + title+".*$"));
        return mongoTemplate.find(query,Simple.class);
    }

    public List<Simple> findByDateLessThan(LocalDate localDate){
        Query query = new Query();
        query.addCriteria(Criteria.where("date").lt(localDate));
        return mongoTemplate.find(query,Simple.class);
    }

    public List<Simple> findByDateMoreThan(LocalDate localDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("date").gt(localDate)).with(Sort.by(Sort.Direction.DESC,"date"));
        return mongoTemplate.find(query,Simple.class);
    }

    public int getToalCount() {
        Query query = new Query();
        return (int)mongoTemplate.count(query,Simple.class);
    }

    public List<Simple> getTitles() {
        Query query = new Query();
        query.fields().include("title").exclude("_id");
        return mongoTemplate.find(query,Simple.class);
    }

    public List<Simple> findTop3() {
        Query query =new Query();
        query.limit(3).with(Sort.by(Sort.Direction.DESC,"date"));
        return mongoTemplate.find(query,Simple.class);
    }

    public List<Simple> findWithPaging(int index) {

        Pageable pageable = PageRequest.of(index,10,Sort.by(Sort.Direction.DESC,"date"));

        Query query = new Query();

        query.with(pageable);

        return mongoTemplate.find(query,Simple.class);
    }
}
