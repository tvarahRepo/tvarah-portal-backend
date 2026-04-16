package com.tvarah.model.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class JobDescriptionResponse {

    private UUID id;
    private String code;
    private String company;
    private String jobTitle;
    private String jobType;
    private String jobMode;
    private String jobLevel;
    private String jobDescriptionText;
    private List<String> summaryResponsibilities;
    private BigDecimal experienceMinYrs;
    private BigDecimal experienceMaxYrs;
    private List<String> requiredSkills;
    private List<String> goodToHaveSkills;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private Integer totalPositions;
    private Integer totalPositionsSelected;
    private Integer totalRounds;
    private String status;
    private Instant closedOn;
    private Instant createdOn;
    private Instant updatedOn;
    private String createdBy;
    private String updatedBy;
}
