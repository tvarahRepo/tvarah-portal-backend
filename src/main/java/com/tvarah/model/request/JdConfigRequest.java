package com.tvarah.model.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class JdConfigRequest {

    private JsonNode jdConfig;
    private List<UUID> skillsDomainSpecific;
    private List<UUID> mustHaveSkills;
    private Boolean useConfigMustHave;
}
