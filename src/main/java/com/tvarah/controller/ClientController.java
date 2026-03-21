package com.tvarah.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
@Tag(name = "Client Management", description = "APIs for managing hiring clients, industry classification, and recruiter assignments")
public class ClientController {

}
