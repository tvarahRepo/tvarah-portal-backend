package com.tvarah.model.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CompanyRequest {

    private String name;
    private String nameAlias;
    private String type;
    private String industry;
    private String headquarterCity;
    private String headquarterState;
    private String headquarterCountry;
    private Integer foundedYear;
    private Integer noOfEmployees;
    private BigDecimal avgRating;
    private Boolean mcaVerified;
    private String status;
    private String sector;
    private String companyDomain;
    private String candidatePersonalDomain;
    private String domainSource;
}
