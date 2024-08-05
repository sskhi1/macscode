package com.freeuni.macs.service;

import com.freeuni.macs.mapper.UserSubmissionMapper;
import com.freeuni.macs.model.UserSubmission;
import com.freeuni.macs.model.api.UserSubmissionDto;
import com.freeuni.macs.repository.UserSubmissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserSubmissionService {

    private final UserSubmissionRepository userSubmissionRepository;

    @Autowired
    public UserSubmissionService(UserSubmissionRepository userSubmissionRepository, UserSubmissionMapper userSubmissionMapper) {
        this.userSubmissionRepository = userSubmissionRepository;
    }

    public List<UserSubmissionDto> getUserSubmissions(final String username) {
        List<UserSubmission> userSubmissions = userSubmissionRepository.findAllBySubmitterUsernameOrderBySubmissionDateDesc(username);

        return UserSubmissionMapper.toDtoList(userSubmissions);
    }

    public List<UserSubmissionDto> getProblemSubmissions(String problemId) {
        ObjectId id = new ObjectId(problemId);
        List<UserSubmission> problemSubmissions = userSubmissionRepository.findAllByProblemIdOrderBySubmissionDateDesc(id);

        return UserSubmissionMapper.toDtoList(problemSubmissions);
    }

    public List<UserSubmissionDto> getProblemUserSubmissions(String problemId, String username) {
        ObjectId id = new ObjectId(problemId);
        List<UserSubmission> problemUserSubmissions = userSubmissionRepository.findAllBySubmitterUsernameAndProblemIdOrderBySubmissionDateDesc(username, id);

        return UserSubmissionMapper.toDtoList(problemUserSubmissions);
    }
}
