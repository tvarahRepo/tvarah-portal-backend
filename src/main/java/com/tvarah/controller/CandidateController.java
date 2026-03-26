package com.tvarah.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidate Management", description = "APIs for managing candidate profiles, experience, education, skills, scoring, and pipeline tracking")
public class CandidateController {

    @GetMapping
    @Operation(summary = "List candidates", description = "Returns a paginated list of candidates for the dashboard. Supports filtering by status, skills, experience range, and notice period.")
    public ResponseEntity<?> listCandidates() {
        return null;
    }

    @PostMapping
    @Operation(summary = "Create a candidate", description = "Creates a new candidate profile with identity, contact, work history, education, skills, CTC preferences, and notice period details.")
    public ResponseEntity<?> createCandidate(@RequestBody Object request) {
        return null;
    }

    @PatchMapping("/{candidateId}")
    @Operation(summary = "Update candidate fields", description = "Partially updates one or more fields of an existing candidate profile. Only provided fields are modified.")
    public ResponseEntity<?> updateCandidate(@PathVariable String candidateId, @RequestBody Object request) {
        return null;
    }

    @DeleteMapping("/{candidateId}")
    @Operation(summary = "Delete a candidate", description = "Permanently deletes a candidate and all associated records including applications and interview history.")
    public ResponseEntity<?> deleteCandidate(@PathVariable String candidateId) {
        return null;
    }

    @GetMapping("/{candidateId}/jd-matches")
    @Operation(summary = "Get JD match records", description = "Retrieves all JD-fit evaluation records for a candidate, including skill match, domain alignment, and experience relevance scores across applied job descriptions.")
    public ResponseEntity<?> getJdMatches(@PathVariable String candidateId) {
        return null;
    }

}
