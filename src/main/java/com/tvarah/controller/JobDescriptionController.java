package com.tvarah.controller;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Parse and create a job description from a document",
            description = "Sends the uploaded JD file to the ML parse API, validates the extracted fields against the DB schema's non-null constraints, and saves the record on success. If any mandatory field could not be extracted, returns 400 with the list of missing field names instead of saving."
    )
    public ResponseEntity<ApiResponse<JobDescriptionResponse>> createJobDescription(
            @RequestPart("file") MultipartFile file,
            @RequestParam UUID companyId,
            @RequestParam(required = false) Integer totalPositions,
            @RequestParam(required = false) Integer totalRounds,
            @RequestParam(required = false) UUID assignedToUserId,
            @RequestParam(required = false) String status) {

        JobDescriptionResponse response = jobDescriptionService.create(file, companyId, totalPositions, totalRounds, assignedToUserId, status);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job description created successfully", response));
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
