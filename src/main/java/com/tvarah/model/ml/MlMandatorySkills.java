package com.tvarah.model.ml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MlMandatorySkills {

    @JsonProperty("programming_languages")
    private List<String> programmingLanguages;

    @JsonProperty("frameworks_and_libraries")
    private List<String> frameworksAndLibraries;

    @JsonProperty("tools")
    private List<String> tools;

    @JsonProperty("databases")
    private List<String> databases;

    @JsonProperty("cloud_and_infra")
    private List<String> cloudAndInfra;
}
