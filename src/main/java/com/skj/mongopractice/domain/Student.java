package com.skj.mongopractice.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document("students")
public class Student {
    @MongoId
    private ObjectId id;

    @Field("student_id")
    private String studentId;

    //학년
    private Integer grade;

    //학생 이름
    private String sName;

    //many to one
    //collection 기본값 의존
    // one to Squillions
    // 졸업하고 난후 지도교수가 계속 학적부에 저장되어 있음으로, 1: N 관계에서 one to many로 할 시,
    // professor 도큐먼트에 엄청난 배열이 생길것을 방지
    @DocumentReference(lazy = false)
    @Field(write = Field.Write.ALWAYS)
    private Professor advisorProfessor;
}
