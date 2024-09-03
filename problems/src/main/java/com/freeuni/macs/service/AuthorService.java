package com.freeuni.macs.service;

import com.freeuni.macs.mapper.ProblemRequestMapper;
import com.freeuni.macs.model.*;
import com.freeuni.macs.model.api.InputOutputTestDto;
import com.freeuni.macs.repository.ProblemDraftRepository;
import com.freeuni.macs.repository.ProblemRepository;
import com.freeuni.macs.repository.TestRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final ProblemRepository problemRepository;
    private final TestRepository testRepository;
    private final ProblemDraftRepository problemDraftRepository;

    @Autowired
    public AuthorService(ProblemRepository problemRepository, TestRepository testRepository, ProblemDraftRepository problemDraftRepository) {
        this.problemRepository = problemRepository;
        this.testRepository = testRepository;
        this.problemDraftRepository = problemDraftRepository;
    }

    public void processUpload(UploadProblemRequest uploadProblemRequest) {
        DraftProblem draftProblem = ProblemRequestMapper.convertToDraftProblem(uploadProblemRequest);
        problemDraftRepository.save(draftProblem);
    }

    public void changeProblem(String id, UploadProblemRequest uploadProblemRequest) {
        ObjectId objectId = new ObjectId(id);
        problemDraftRepository.deleteById(objectId);
        DraftProblem newDraftProblem = ProblemRequestMapper.convertToDraftProblem(uploadProblemRequest);
        newDraftProblem.setId(id);
        problemDraftRepository.save(newDraftProblem);
    }


    public void deleteDraft(String id) {
        ObjectId objectId = new ObjectId(id);
        problemDraftRepository.deleteById(objectId);
    }

    public List<DraftProblem> getAllDraftProblems() {
        return problemDraftRepository.findAll();
    }

    public void publishProblem(UploadProblemRequest uploadProblemRequest, String id) {
        if (id != null)
            deleteDraft(id);
        uploadProblemRequest.getTestCases().get(0).setPublic(true);
        Problem problem = saveProblem(uploadProblemRequest);
        saveTestCases(problem.getId(), uploadProblemRequest.getTestCases());
    }

    private Problem saveProblem(UploadProblemRequest request) {
        Problem existingProblem = problemRepository.findTopByProblemIdCourseOrderByProblemIdOrderDesc(getCourse(request.getType()));
        long newOrder = (existingProblem != null) ? existingProblem.getProblemId().getOrder() + 1 : 1;

        Problem problem = new Problem();
        problem.setName(request.getName());
        problem.setDescription(request.getDescription());
        problem.setType(request.getType());
        problem.setTopics(request.getTopics());
        problem.setDifficulty(request.getDifficulty());
        problem.setMainFile(request.getMainFile());
        problem.setSolutionFileTemplate(request.getSolutionTemplateFile());

        problem.setProblemId(new ProblemId(newOrder, getCourse(request.getType())));

        return problemRepository.save(problem);
    }

    private Course getCourse(String type) {
        if ("KAREL".equals(type)) {
            return Course.KAREL;
        } else if ("CPP".equals(type)) {
            return Course.ABS;
        } else {
            return Course.MET;
        }
    }

    private void saveTestCases(ObjectId problemId, List<InputOutputTestDto> testCases) {
        for (InputOutputTestDto testCaseDto : testCases) {
            Test testCase = new Test();
            testCase.setProblemId(problemId);
            testCase.setInput(testCaseDto.getInput());
            testCase.setOutput(testCaseDto.getOutput());
            testCase.setIsPublic(testCaseDto.isPublic());
            testCase.setTestNum(testCaseDto.getTestNum());

            testRepository.save(testCase);
        }
    }
}
