package com.tvarah.model.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CompanyResponse {

    private UUID id;
    private String name;
    private String nameAlias;
    private String type;
    private String industry;
    private String headquarterCity;
    private String headquarterState;
    private String headquarterCountry;
    private Integer foundedYear;
    private Integer noOfEmployees;
    private Integer reviewCount;
    private BigDecimal avgRating;
    private Boolean mcaVerified;
    private String status;
    private String sector;
    private String companyDomain;
    private String candidatePersonalDomain;
    private String domainSource;
    private String logoUrl;

    // ── Contact person (Draft user linked to this company) ────────────────────
    private String contactFirstName;
    private String contactLastName;
    private String contactEmail;
    private String contactPhone;
    private String contactRole;
}
