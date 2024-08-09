package com.freeuni.macs.mapper;

import com.freeuni.macs.model.Problem;
import com.freeuni.macs.model.api.ProblemDto;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class ProblemMapper {

    public static ProblemDto toDto(Problem problem) {
        ProblemDto problemDto = new ProblemDto();
        problemDto.setId(problem.getId().toString());
        problemDto.setProblemId(problem.getProblemId());
        problemDto.setName(problem.getName());
        problemDto.setDescription(problem.getDescription());
        problemDto.setDifficulty(problem.getDifficulty());
        problemDto.setTopics(problem.getTopics());
        problemDto.setType(problem.getType());
        return problemDto;
    }

    public static Problem toEntity(ProblemDto problemDto) {
        Problem problem = new Problem();
        problem.setId(new ObjectId(problemDto.getId()));
        problem.setProblemId(problemDto.getProblemId());
        problem.setName(problemDto.getName());
        problem.setDescription(problemDto.getDescription());
        problem.setDifficulty(problemDto.getDifficulty());
        problem.setTopics(problemDto.getTopics());
        problem.setType(problemDto.getType());
        return problem;
    }
}
