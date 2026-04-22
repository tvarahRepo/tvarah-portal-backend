package com.tvarah.model.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Data
@Builder
public class JdCandidateResponse {

    private UUID candidateId;
    private String firstName;
    private String lastName;
    private String primaryEmail;
    private String primaryPhoneNumber;
    private String city;
    private String country;
    private String status;
    private String designation;
    private String currentCompany;
    private BigDecimal totalExperience;
    private BigDecimal resumeScore;
    private CandidateEvaluationResponse evaluation;
    private List<CandidateInterviewResponse> interviews;
}
