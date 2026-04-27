package com.tvarah.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "candidate_preference")
public class CandidatePreference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "current_location", length = 100)
    private String currentLocation;

    @Column(name = "primary_preferred_location", length = 100)
    private String primaryPreferredLocation;

    @Column(name = "preferred_locations", columnDefinition = "TEXT[]")
    private List<String> preferredLocations;

    @Column(name = "can_relocate", nullable = false)
    private Boolean canRelocate = false;

    @Column(name = "preferred_job_title_id")
    private UUID preferredJobTitleId;

    @Column(name = "current_ctc", precision = 12, scale = 2)
    private BigDecimal currentCtc;

    @Column(name = "expected_ctc_min", precision = 12, scale = 2)
    private BigDecimal expectedCtcMin;

    @Column(name = "expected_ctc_max", precision = 12, scale = 2)
    private BigDecimal expectedCtcMax;

    @Column(name = "ctc_currency", nullable = false, length = 5)
    private String ctcCurrency;

    @Column(name = "work_mode_preference", length = 50)
    private String workModePreference;

    @Column(name = "work_authorization", nullable = false)
    private Boolean workAuthorization = false;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}
