package com.freeuni.macs.mapper;

import com.freeuni.macs.model.UserSubmission;
import com.freeuni.macs.model.api.UserSubmissionDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSubmissionMapper {

    public static UserSubmissionDto toDto(UserSubmission userSubmission) {
        if (userSubmission == null) {
            return null;
        }

        return UserSubmissionDto.builder()
                .id(userSubmission.getId())
                .submitterUsername(userSubmission.getSubmitterUsername())
                .problem(ProblemMapper.toDto(userSubmission.getProblem()))
                .solutionFileContent(userSubmission.getSolutionFileContent())
                .submissionDate(userSubmission.getSubmissionDate())
                .result(userSubmission.getResult())
                .build();
    }

    public static UserSubmission toEntity(UserSubmissionDto userSubmissionDto) {
        if (userSubmissionDto == null) {
            return null;
        }

        return UserSubmission.builder()
                .id(userSubmissionDto.getId())
                .submitterUsername(userSubmissionDto.getSubmitterUsername())
                .problem(ProblemMapper.toEntity(userSubmissionDto.getProblem()))
                .solutionFileContent(userSubmissionDto.getSolutionFileContent())
                .submissionDate(userSubmissionDto.getSubmissionDate())
                .result(userSubmissionDto.getResult())
                .build();
    }

    public static List<UserSubmissionDto> toDtoList(List<UserSubmission> userSubmissions) {
        if (userSubmissions == null || userSubmissions.isEmpty()) {
            return List.of();
        }

        return userSubmissions.stream()
                .map(UserSubmissionMapper::toDto)
                .collect(Collectors.toList());
    }

    public static List<UserSubmission> toEntityList(List<UserSubmissionDto> userSubmissionDtos) {
        if (userSubmissionDtos == null || userSubmissionDtos.isEmpty()) {
            return List.of();
        }

        return userSubmissionDtos.stream()
                .map(UserSubmissionMapper::toEntity)
                .collect(Collectors.toList());
    }
}
