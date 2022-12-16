package com.skj.mongopractice.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "subjects")
@Data
public class Subject {
    @Id
    private ObjectId id;

    private String sId;
    //강의명
    private String sName;

    //학점
    private Integer point;


    //이 강의를 강의하는 교수님
    // one to Squillions
    @DocumentReference
    private Professor professor;
}
