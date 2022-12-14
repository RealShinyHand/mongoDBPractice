package com.skj.mongopractice.domain;

import com.skj.mongopractice.constant.HireType;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "professors")
public class Professor {
    //몽고 db에서 사용하는 ID 값,String 혹은 Long 으로 사용하면 Convertor를 통해 _id:ObjectId로도 변환된다.
    @Id
    private ObjectId id;

    private String pName;

    private Integer salary;

    private String hireData;

    private HireType hireType;

    private String expireDate;


}
