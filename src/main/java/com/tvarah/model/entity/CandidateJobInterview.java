package com.tvarah.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "candidate_job_interview")
public class CandidateJobInterview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "candidate_job_id", nullable = false)
    private UUID candidateJobId;

    @Column(name = "round_number", nullable = false)
    private Integer roundNumber;

    @Column(name = "interviewer_name", length = 200)
    private String interviewerName;

    @Column(name = "interviewer_email", length = 200)
    private String interviewerEmail;

    @Column(name = "interviewer_type", length = 50)
    private String interviewerType;

    @Column(name = "scheduled_on", nullable = false)
    private Instant scheduledOn;

    @Column(name = "mode", length = 50)
    private String mode;

    @Column(name = "status", nullable = false, length = 100)
    private String status;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "questions", columnDefinition = "text[]")
    private List<String> questions;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "focus_areas", columnDefinition = "text[]")
    private List<String> focusAreas;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "max_score", precision = 5, scale = 2)
    private BigDecimal maxScore;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
