package com.tvarah.service;

import com.tvarah.model.ml.MlJdParseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MlJdParseClient {

    MlJdParseResponse parse(MultipartFile file);
}
