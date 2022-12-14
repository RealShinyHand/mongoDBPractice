package com.skj.mongopractice.service;

import com.skj.mongopractice.domain.Simple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
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
}
