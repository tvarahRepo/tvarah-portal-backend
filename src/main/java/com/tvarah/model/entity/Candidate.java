package com.tvarah.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "candidate")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "first_name", nullable = false, length = 150)
    private String firstName;

    @Column(name = "middle_name", length = 150)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 150)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "primary_email", unique = true, length = 255)
    private String primaryEmail;

    @Column(name = "secondary_email", length = 255)
    private String secondaryEmail;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", length = 150)
    private String city;

    @Column(name = "country", length = 150)
    private String country;

    @Column(name = "country_code", length = 10)
    private String countryCode;

    @Column(name = "primary_phone_number", length = 20)
    private String primaryPhoneNumber;

    @Column(name = "secondary_phone_number", length = 20)
    private String secondaryPhoneNumber;

    @Column(name = "avatar_color", length = 20)
    private String avatarColor;

    @Column(name = "starred", nullable = false)
    private Boolean starred = false;

    @Column(name = "status", nullable = false, length = 100)
    private String status;

    @Column(name = "job_title_id", nullable = false)
    private UUID jobTitleId;

    @Column(name = "designation_id", nullable = false)
    private UUID designationId;

    @Column(name = "current_company_id", nullable = false)
    private UUID currentCompanyId;

    @Column(name = "total_experience", precision = 12, scale = 2)
    private BigDecimal totalExperience;

    @Column(name = "relevant_experience", precision = 12, scale = 2)
    private BigDecimal relevantExperience;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "created_by", nullable = false, length = 200)
    private String createdBy;

    @Column(name = "updated_by", length = 200)
    private String updatedBy;
}
