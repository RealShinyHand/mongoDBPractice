package com.skj.mongopractice.repository;

import com.skj.mongopractice.domain.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubjectRepository extends MongoRepository<Subject,String> {
}
