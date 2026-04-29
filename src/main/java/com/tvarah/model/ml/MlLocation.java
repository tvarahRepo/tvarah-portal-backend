package com.tvarah.model.ml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MlLocation {

    private String city;
    private String country;
}
