package com.tvarah.service.impl;

import com.tvarah.exception.BadRequestException;
import com.tvarah.model.ml.MlJdParseResponse;
import com.tvarah.service.MlJdParseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class MlJdParseClientImpl implements MlJdParseClient {

    private final RestTemplate restTemplate;
    private final String parseUrl;

    public MlJdParseClientImpl(@Value("${app.ml.jd-parse-url}") String parseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);
        factory.setReadTimeout(120_000);
        this.restTemplate = new RestTemplate(factory);
        this.parseUrl = parseUrl;
    }

    @Override
    public MlJdParseResponse parse(MultipartFile file) {
        log.info("Calling ML JD parse API for file: {}", file.getOriginalFilename());

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new BadRequestException("Failed to read uploaded file: " + e.getMessage());
        }

        ByteArrayResource fileResource = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<MlJdParseResponse> response = restTemplate.postForEntity(
                    parseUrl, request, MlJdParseResponse.class);

            log.info("ML JD parse API responded with status: {}", response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            log.error("ML JD parse API call failed: {}", e.getMessage());
            throw new BadRequestException("ML service unavailable: " + e.getMessage());
        }
    }
}
