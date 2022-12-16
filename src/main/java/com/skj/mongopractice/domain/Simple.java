package com.skj.mongopractice.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@Data
@Document(value="simples")
public class Simple {

    @Builder
    public Simple(String title,String body,LocalDate date){
        this.title = title;
        this.body = body;
        this.date = date;
    }
    @MongoId
    private String id;

    private String title;

    private String body;

    private LocalDate date;
}
