package com.skj.mongopractice.service;

import com.skj.mongopractice.dto.ProfessorDto;

import java.util.List;

public interface ProfessorService {
    List<ProfessorDto> findAll();

}
