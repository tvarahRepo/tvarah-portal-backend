package com.tvarah.model.ml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MlJdData {

    @JsonProperty("role_title")
    private String roleTitle;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("salary_range")
    private JsonNode salaryRange;

    @JsonProperty("location")
    private List<MlLocation> location;

    @JsonProperty("job_type")
    private String jobType;

    @JsonProperty("job_level")
    private String jobLevel;

    @JsonProperty("work_mode")
    private String workMode;

    @JsonProperty("mandatory_skills")
    private MlMandatorySkills mandatorySkills;

    @JsonProperty("optional_skills")
    private List<String> optionalSkills;

    @JsonProperty("min_years_experience")
    private BigDecimal minYearsExperience;

    @JsonProperty("max_years_experience")
    private BigDecimal maxYearsExperience;

    @JsonProperty("degree_required")
    private String degreeRequired;

    @JsonProperty("team_size")
    private Integer teamSize;

    @JsonProperty("industry_domains")
    private List<String> industryDomains;

    @JsonProperty("summary_responsibilities")
    private List<String> summaryResponsibilities;
}
