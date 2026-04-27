package com.tvarah.controller;

import com.tvarah.model.response.ApiResponse;
import com.tvarah.model.response.CandidateResponse;
import com.tvarah.service.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidate")
@RequiredArgsConstructor
@Tag(name = "Candidate Management", description = "APIs for managing candidate profiles, experience, education, skills, scoring, and pipeline tracking")
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping
    @Operation(
            summary = "List all candidates",
            description = "Returns all candidates with their profile details including job title, designation, current company, and experience."
    )
    public ResponseEntity<ApiResponse<List<CandidateResponse>>> listCandidates() {
        return ResponseEntity.ok(ApiResponse.success(candidateService.findAll()));
    }
}
