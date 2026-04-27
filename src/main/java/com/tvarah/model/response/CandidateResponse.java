package com.tvarah.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateResponse {

    private UUID id;
    private String code;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String primaryEmail;
    private String secondaryEmail;
    private String primaryPhoneNumber;
    private String secondaryPhoneNumber;
    private String address;
    private String city;
    private String country;
    private String avatarColor;
    private Boolean starred;
    private String status;
    private String jobTitle;
    private String designation;
    private String currentCompany;
    private BigDecimal totalExperience;
    private BigDecimal relevantExperience;
    private Instant createdOn;
    private Instant updatedOn;

    // Work preference (candidate_preference)
    private String workModePreference;
    private Boolean canRelocate;
    private Boolean workAuthorization;
    private BigDecimal currentCtc;
    private BigDecimal expectedCtcMin;
    private BigDecimal expectedCtcMax;
    private String ctcCurrency;

    // Notice period (candidate_notice_period)
    private Boolean isInNoticePeriod;
    private Short noticePeriodDuration;
    private Instant lastWorkingDate;
    private Instant earliestJoiningDate;

    // Resume score (candidate_score)
    private BigDecimal resumeScore;
    private BigDecimal resumeMaxScore;
    private String strength;
    private Boolean dropFlag;
    private String fraudRisk;
}
