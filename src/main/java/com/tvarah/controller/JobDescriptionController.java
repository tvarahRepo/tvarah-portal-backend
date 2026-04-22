package com.tvarah.controller;

import com.tvarah.model.response.ApiResponse;
import com.tvarah.model.response.JobDescriptionResponse;
import com.tvarah.service.JobDescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-descriptions")
@RequiredArgsConstructor
@Tag(name = "JD Management", description = "APIs for managing job descriptions, skill requirements, salary bands, and position tracking")
public class JobDescriptionController {

    private final JobDescriptionService jobDescriptionService;

    @GetMapping
    @Operation(
            summary = "List all job descriptions",
            description = "Returns all job descriptions with full details including skills, salary bands, experience range, and position counts."
    )
    public ResponseEntity<ApiResponse<List<JobDescriptionResponse>>> listJobDescriptions() {
        return ResponseEntity.ok(ApiResponse.success(jobDescriptionService.findAll()));
    }


}
