package com.tvarah.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job-descriptions")
@Tag(name = "JD Management", description = "APIs for managing job descriptions, skill requirements, salary bands, and position tracking")
public class JobDescriptionController {

    @GetMapping
    @Operation(
            summary = "List job descriptions",
            description = "Returns a paginated list of job descriptions for the dashboard. Supports filtering by client, status, experience band, and skill requirements."
    )
    public ResponseEntity<?> listJobDescriptions() {
        return null;
    }

    @PostMapping
    @Operation(
            summary = "Create a job description",
            description = "Creates a new JD with skill arrays (required / must-have / good-to-have), experience band, salary range, round configuration, and non-negotiable rules."
    )
    public ResponseEntity<?> createJobDescription(@RequestBody Object request) {
        return null;
    }

    @PatchMapping("/{jdId}")
    @Operation(
            summary = "Update job description fields",
            description = "Partially updates one or more fields of an existing job description. Only provided fields are modified."
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
            description = "Retrieves all candidates applied or mapped to a specific job description, along with their pipeline stage and JD-fit evaluation scores."
    )
    public ResponseEntity<?> getCandidatesForJd(@PathVariable String jdId) {
        return null;
    }

}
