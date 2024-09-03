package com.freeuni.macs.mapper;

import com.freeuni.macs.model.DraftProblem;
import com.freeuni.macs.model.UploadProblemRequest;
import org.springframework.stereotype.Component;

@Component
public class ProblemRequestMapper {

    public static DraftProblem convertToDraftProblem(UploadProblemRequest request) {
        DraftProblem draftProblem = new DraftProblem();
        draftProblem.setName(request.getName());
        draftProblem.setDescription(request.getDescription());
        draftProblem.setType(request.getType());
        draftProblem.setDifficulty(request.getDifficulty());
        draftProblem.setTopics(request.getTopics());
        draftProblem.setSolutionTemplateFile(request.getSolutionTemplateFile());
        draftProblem.setMainFile(request.getMainFile());
        draftProblem.setTestCases(request.getTestCases());

        return draftProblem;
    }

}
