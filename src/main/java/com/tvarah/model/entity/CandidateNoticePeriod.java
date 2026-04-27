package com.tvarah.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "candidate_notice_period")
public class CandidateNoticePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "is_in_notice_period", nullable = false)
    private Boolean isInNoticePeriod = false;

    @Column(name = "notice_period_duration", nullable = false)
    private Short noticePeriodDuration;

    @Column(name = "resignation_date")
    private Instant resignationDate;

    @Column(name = "earliest_joining_date")
    private Instant earliestJoiningDate;

    @Column(name = "last_working_date")
    private Instant lastWorkingDate;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}
