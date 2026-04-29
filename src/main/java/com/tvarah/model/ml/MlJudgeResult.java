package com.tvarah.model.ml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MlJudgeResult {

    private String source;
    private String grade;
    private String summary;
}
