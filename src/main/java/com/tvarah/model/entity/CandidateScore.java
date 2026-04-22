package com.tvarah.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "candidate_score")
public class CandidateScore {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "max_score", precision = 5, scale = 2)
    private BigDecimal maxScore;

    @Column(name = "strength", length = 50)
    private String strength;

    @Column(name = "feedback_comments", columnDefinition = "TEXT")
    private String feedbackComments;

    @Column(name = "drop_flag", nullable = false)
    private Boolean dropFlag = false;

    @Column(name = "drop_flag_message", columnDefinition = "TEXT")
    private String dropFlagMessage;

    @Column(name = "fraud_risk", length = 50)
    private String fraudRisk;

    @Column(name = "fraud_status", nullable = false)
    private Boolean fraudStatus = false;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}
