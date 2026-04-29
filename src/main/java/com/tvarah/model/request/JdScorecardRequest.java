package com.tvarah.model.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
public class JdScorecardRequest {

    private Short scorecardRoleClarity;
    private Short scorecardTechSpecificity;
    private String consultingVsProduct;
    private String experienceInflation;
    private List<String> missingScreening;
    private String compensationSignal;
    private String mlVerdict;
    private JsonNode judgeResults;
    private Short mlReflectionLoop;
}
