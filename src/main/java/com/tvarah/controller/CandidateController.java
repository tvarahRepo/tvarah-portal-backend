package com.tvarah.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/candidates")
@Tag(name = "Candidate Management", description = "APIs for managing candidate profiles, experience, education, skills, scoring, and pipeline tracking")
public class CandidateController {

}
