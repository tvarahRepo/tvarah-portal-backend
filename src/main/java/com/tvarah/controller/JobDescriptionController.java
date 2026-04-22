package com.tvarah.controller;

import com.tvarah.model.response.ApiResponse;
import com.tvarah.model.response.JdCandidateResponse;
import com.tvarah.model.response.JobDescriptionResponse;
import com.tvarah.service.JobDescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
