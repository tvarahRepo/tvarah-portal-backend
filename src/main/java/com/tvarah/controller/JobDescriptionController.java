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

    @PostMapping
    @Operation(
            summary = "Create a job description",
            description = "Creates a new JD with skill arrays (required / good-to-have), experience band, salary range, round configuration."
    )
    public ResponseEntity<?> createJobDescription(@RequestBody Object request) {
        return null;
    }

    @PatchMapping("/{jdId}")
    @Operation(
            summary = "Update job description fields",
            description = "Partially updates one or more fields of an existing job description."
    )
    public ResponseEntity<?> updateJobDescription(@PathVariable String jdId, @RequestBody Object request) {
        return null;
    }

    @DeleteMapping("/{jdId}")
    @Operation(
            summary = "Delete a job description",
            description = "Permanently deletes a job description and all associated application pipeline records."
    )
    public ResponseEntity<?> deleteJobDescription(@PathVariable String jdId) {
        return null;
    }

    @GetMapping("/{jdId}/candidates")
    @Operation(
            summary = "Get candidates for a JD",
            description = "Retrieves all candidates mapped to a specific job description along with their pipeline stage and JD-fit evaluation scores."
    )
    public ResponseEntity<?> getCandidatesForJd(@PathVariable String jdId) {
        return null;
    }
}
