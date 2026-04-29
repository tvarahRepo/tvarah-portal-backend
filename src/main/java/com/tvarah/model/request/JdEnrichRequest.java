package com.tvarah.model.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
public class JdEnrichRequest {

    private String fieldOfStudy;
    private List<String> certificationsRequired;
    private List<String> certificationsGoodToHave;
    private String ctcRange;
    private String payFrequency;
    private String equityEsop;
    private List<String> benefits;
    private String department;
    private String reportsTo;
    private String industryDomain;
    private List<String> preferredPriorRoles;
    private List<String> preferredCompanyTypes;
    private String roleSummary;
    private List<String> keyResponsibilities;
    private String domainExpertise;
    private List<String> skillsBehavioural;
    private JsonNode enrichedJd;
}
