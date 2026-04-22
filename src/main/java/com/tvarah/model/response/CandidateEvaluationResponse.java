package com.tvarah.model.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CandidateEvaluationResponse {

    private UUID id;
    private BigDecimal jdOverallMatchScore;
    private BigDecimal skillMatchScore;
    private BigDecimal domainMatchScore;
    private BigDecimal jdExpRelevanceScore;
    private BigDecimal score;
    private BigDecimal maxScore;
    private String status;
}
