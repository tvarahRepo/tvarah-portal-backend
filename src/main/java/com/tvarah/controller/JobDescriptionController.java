package com.tvarah.controller;

import com.tvarah.model.dto.JdDraftDto;
import com.tvarah.model.request.JdConfigRequest;
import com.tvarah.model.request.JdEnrichRequest;
import com.tvarah.model.request.JdScorecardRequest;
import com.tvarah.model.response.ApiResponse;
import com.tvarah.model.response.JdCandidateResponse;
import com.tvarah.model.response.JobDescriptionResponse;
import com.tvarah.service.JobDescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/job-descriptions")
@RequiredArgsConstructor
@Tag(name = "JD Management", description = "APIs for managing job descriptions, skill requirements, salary bands, and position tracking")
public class JobDescriptionController {

    private final JobDescriptionService jobDescriptionService;

    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Parse a JD document via ML",
            description = "Accepts a JD file (PDF/DOCX), sends it to the ML API, maps the response to the DB schema shape, and returns it for UI review. Nothing is saved to the database."
    )
    public ResponseEntity<ApiResponse<JdDraftDto>> parseJobDescription(
            @RequestPart("file") MultipartFile file) {

        JdDraftDto draft = jobDescriptionService.parse(file);
        return ResponseEntity.ok(ApiResponse.success("JD parsed successfully", draft));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a job description from reviewed draft",
            description = "Accepts the DB-schema-aligned draft (returned by /parse and optionally modified by the UI) and persists it as a new job description in Draft status."
    )
    public ResponseEntity<ApiResponse<JobDescriptionResponse>> createJobDescription(
            @RequestBody JdDraftDto draft) {

        JobDescriptionResponse response = jobDescriptionService.create(draft);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job description created successfully", response));
    }

    @PatchMapping("/{id}/enrich")
    @Operation(
            summary = "Enrich a job description",
            description = "Applies OpenAI-enriched fields (department, role summary, compensation, certifications, etc.) to an existing job description."
    )
    public ResponseEntity<ApiResponse<Void>> enrichJobDescription(
            @PathVariable UUID id,
            @RequestBody JdEnrichRequest request) {

        jobDescriptionService.enrich(id, request);
        return ResponseEntity.ok(ApiResponse.success("Job description enriched successfully", null));
    }

    @PatchMapping("/{id}/config")
    @Operation(
            summary = "Save JD scoring configuration",
            description = "Stores the JD config (weights, thresholds, filters, skill groups) and optional domain-specific skills set by the recruiter."
    )
    public ResponseEntity<ApiResponse<Void>> configureJobDescription(
            @PathVariable UUID id,
            @RequestBody JdConfigRequest request) {

        jobDescriptionService.configure(id, request);
        return ResponseEntity.ok(ApiResponse.success("Job description config saved successfully", null));
    }

    @PatchMapping("/{id}/scorecard")
    @Operation(
            summary = "Update JD scorecard and ML verdict",
            description = "Persists the ML quality scorecard (role clarity, tech specificity, etc.) and verdict for a job description."
    )
    public ResponseEntity<ApiResponse<Void>> updateScorecard(
            @PathVariable UUID id,
            @RequestBody JdScorecardRequest request) {

        jobDescriptionService.updateScorecard(id, request);
        return ResponseEntity.ok(ApiResponse.success("Scorecard updated successfully", null));
    }

    @GetMapping
    @Operation(
            summary = "List all job descriptions",
            description = "Returns all job descriptions with full details including skills, salary bands, experience range, and position counts."
    )
    public ResponseEntity<ApiResponse<List<JobDescriptionResponse>>> listJobDescriptions() {
        return ResponseEntity.ok(ApiResponse.success(jobDescriptionService.findAll()));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Cancel a job description",
            description = "Marks a job description as Cancelled. Does not delete the record."
    )
    public ResponseEntity<ApiResponse<Void>> cancelJobDescription(@PathVariable UUID id) {
        jobDescriptionService.cancelJobDescription(id);
        return ResponseEntity.ok(ApiResponse.success("Job description cancelled successfully", null));
    }

    @GetMapping("/{id}/candidates")
    @Operation(
            summary = "Get candidates for a job description",
            description = "Returns all candidates matched to a job description along with their evaluation scores, interview details, and resume score. Results are ordered by JD overall match score in descending order."
    )
    public ResponseEntity<ApiResponse<List<JdCandidateResponse>>> getCandidatesForJd(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(jobDescriptionService.getCandidatesForJd(id)));
    }
}
