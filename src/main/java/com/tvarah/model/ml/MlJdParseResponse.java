package com.tvarah.model.ml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MlJdParseResponse {

    @JsonProperty("jd_data")
    private MlJdData jdData;

    @JsonProperty("judge_results")
    private List<MlJudgeResult> judgeResults;

    @JsonProperty("reflection_loop")
    private Integer reflectionLoop;

    @JsonProperty("verdict")
    private String verdict;
}
