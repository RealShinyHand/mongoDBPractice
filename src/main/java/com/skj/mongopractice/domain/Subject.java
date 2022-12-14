package com.skj.mongopractice.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subjects")
public class Subject {
    @Id
    private ObjectId id;

    //강의명
    private String sName;

    //학점
    private Integer credit;


}
