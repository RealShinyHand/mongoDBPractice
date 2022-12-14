package com.skj.mongopractice.service;

import com.skj.mongopractice.domain.Professor;
import com.skj.mongopractice.dto.ProfessorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService{

    private final MongoTemplate mongoTemplate;

    @Override
    public List<ProfessorDto> findAll() {

        mongoTemplate.findAll(Professor.class).stream();
        return null;
    }
}
