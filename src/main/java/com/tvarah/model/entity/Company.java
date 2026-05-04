package com.tvarah.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "name_alias", length = 200)
    private String nameAlias;

    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "industry", length = 150)
    private String industry;

    @Column(name = "headquarter_city", length = 100)
    private String headquarterCity;

    @Column(name = "headquarter_state", length = 100)
    private String headquarterState;

    @Column(name = "headquarter_country", length = 100)
    private String headquarterCountry;

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Column(name = "no_of_employees")
    private Integer noOfEmployees;

    @Column(name = "review_count", nullable = false)
    private Integer reviewCount;

    @Column(name = "avg_rating", precision = 3, scale = 1)
    private BigDecimal avgRating;

    @Column(name = "mca_verified", nullable = false)
    private Boolean mcaVerified;

    @Column(name = "status", length = 100)
    private String status;

    @Column(name = "sector", length = 100)
    private String sector;

    @Column(name = "company_domain", length = 200)
    private String companyDomain;

    @Column(name = "candidate_personal_domain", length = 200)
    private String candidatePersonalDomain;

    @Column(name = "domain_source", length = 100)
    private String domainSource;
}
