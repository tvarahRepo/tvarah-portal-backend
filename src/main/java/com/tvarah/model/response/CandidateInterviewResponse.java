package com.tvarah.model.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CandidateInterviewResponse {

    private UUID id;
    private Integer roundNumber;
    private String interviewerName;
    private String interviewerEmail;
    private String interviewerType;
    private Instant scheduledOn;
    private String mode;
    private String status;
    private List<String> questions;
    private List<String> focusAreas;
    private String feedback;
    private BigDecimal score;
    private BigDecimal maxScore;
}
