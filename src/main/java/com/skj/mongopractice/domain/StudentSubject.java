package com.skj.mongopractice.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("student_subject")
public class StudentSubject {
    @DocumentReference
    private Student student;
    @DocumentReference
    private Subject subject;

    @Field(write = Field.Write.ALWAYS)
    private String record;
}
