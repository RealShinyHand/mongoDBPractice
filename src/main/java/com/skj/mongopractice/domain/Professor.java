package com.skj.mongopractice.domain;

import com.skj.mongopractice.constant.HireType;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "professors")
public class Professor {
    //몽고 db에서 사용하는 ID 값,String 혹은 Long 으로 사용하면 Convertor를 통해 _id:ObjectId로도 변환된다.
    //MongoId 써도 되고, Id써도 되고...
    @Id
    private ObjectId id;

    //교수 번호
    private String pId;

    //이름
    private String pName;

    //연봉
    private Integer salary;


}
