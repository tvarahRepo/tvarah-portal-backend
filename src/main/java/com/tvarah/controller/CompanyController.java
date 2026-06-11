package com.tvarah.controller;

import com.tvarah.model.request.CompanyRequest;
import com.tvarah.model.response.ApiResponse;
import com.tvarah.model.response.CompanyResponse;
import com.tvarah.model.response.UserResponse;
import com.tvarah.service.CompanyService;
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
@RequestMapping("/companies")
@RequiredArgsConstructor
@Tag(name = "Company Management", description = "APIs for managing companies")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @Operation(summary = "Create a company")
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(@RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Company created successfully", companyService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List all companies")
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> listCompanies() {
        return ResponseEntity.ok(ApiResponse.success(companyService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get company by ID")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompany(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(companyService.findById(id)));
    }

    @PatchMapping(value = "/{id}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload company logo")
    public ResponseEntity<ApiResponse<CompanyResponse>> uploadLogo(
            @PathVariable UUID id,
            @RequestParam("logo") MultipartFile logo) {
        return ResponseEntity.ok(ApiResponse.success("Logo uploaded successfully", companyService.uploadLogo(id, logo)));
    }

    @GetMapping("/{id}/users")
    @Operation(summary = "Get users linked to a company")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getCompanyUsers(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(companyService.findUsersByCompanyId(id)));
    }

    @GetMapping("/{id}/logo")
    @Operation(summary = "Get company logo")
    public ResponseEntity<byte[]> getLogo(@PathVariable UUID id) {
        byte[] image = companyService.getLogo(id);
        String contentType = companyService.getLogoContentType(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(image);
    }
}
