package com.freeuni.macs.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSubmissionDto {

    private ObjectId id;

    private String submitterUsername;

    private ProblemDto problem;

    private String solutionFileContent;

    private Date submissionDate;

    private String result;
}
