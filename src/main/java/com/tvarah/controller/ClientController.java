package com.tvarah.controller;

import com.tvarah.model.entity.CompanyUser;
import com.tvarah.model.entity.User;
import com.tvarah.model.enums.UserStatus;
import com.tvarah.model.request.AddClientRequest;
import com.tvarah.model.request.AddUserRequest;
import com.tvarah.model.request.CompanyRequest;
import com.tvarah.model.response.ApiResponse;
import com.tvarah.model.response.CompanyResponse;
import com.tvarah.repository.CompanyUserRepository;
import com.tvarah.repository.UserRepository;
import com.tvarah.security.SecurityUtils;
import com.tvarah.service.CompanyService;
import com.tvarah.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Client Management", description = "APIs for managing hiring clients, industry classification, and recruiter assignments")
public class ClientController {

    private final CompanyService companyService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CompanyUserRepository companyUserRepository;

    @GetMapping
    @Operation(summary = "List companies assigned to the current user")
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getMyClients() {
        return ResponseEntity.ok(ApiResponse.success(companyService.findByCurrentUser()));
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Add a new client", description = "Creates the company, registers the contact person as a Draft user, and links both to the creating user.")
    public ResponseEntity<ApiResponse<CompanyResponse>> addClient(@Valid @RequestBody AddClientRequest request) {

        // 1 — Create the company
        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setName(request.getName());
        companyRequest.setIndustry(request.getIndustry());
        companyRequest.setHeadquarterCity(request.getHeadquarterCity());
        companyRequest.setHeadquarterState(request.getHeadquarterState());
        companyRequest.setHeadquarterCountry(request.getHeadquarterCountry());
        companyRequest.setCompanyDomain(request.getCompanyDomain());
        companyRequest.setStatus(request.getStatus() != null ? request.getStatus() : "Active");
        companyRequest.setSector(request.getSector());
        companyRequest.setType(request.getType());
        companyRequest.setMcaVerified(false);
        CompanyResponse company = companyService.create(companyRequest);

        // 2 — Create the contact person as a Draft user
        String[] nameParts = request.getContactName().trim().split("\\s+", 2);
        AddUserRequest contactRequest = new AddUserRequest();
        contactRequest.setFirstName(nameParts[0]);
        contactRequest.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        contactRequest.setEmail(request.getContactEmail());
        contactRequest.setPhoneNumber(request.getContactPhone());
        contactRequest.setRole(request.getContactRole());
        userService.createDraftUser(contactRequest);

        // 3 — Link contact person to company
        User contact = userRepository.findByEmail(request.getContactEmail())
                .orElseThrow();
        companyUserRepository.save(CompanyUser.builder()
                .companyId(company.getId())
                .userId(contact.getId())
                .role(request.getContactRole())
                .build());

        // 4 — Link the creating user (Account Manager) to the company
        String keycloakId = SecurityUtils.getCurrentUserId().orElse(null);
        if (keycloakId != null) {
            userRepository.findByKeycloakUserId(keycloakId).ifPresent(owner ->
                companyUserRepository.save(CompanyUser.builder()
                        .companyId(company.getId())
                        .userId(owner.getId())
                        .role("Account Manager")
                        .build())
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Client added successfully", company));
    }
}
