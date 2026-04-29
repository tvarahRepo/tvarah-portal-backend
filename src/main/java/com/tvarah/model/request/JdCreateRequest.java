package com.tvarah.model.request;

import com.tvarah.model.ml.MlJdParseResponse;
import lombok.Data;

import java.util.UUID;

@Data
public class JdCreateRequest {

    private UUID companyId;
    private Integer totalPositions;
    private Integer totalRounds;
    private MlJdParseResponse jdParseResponse;
}
