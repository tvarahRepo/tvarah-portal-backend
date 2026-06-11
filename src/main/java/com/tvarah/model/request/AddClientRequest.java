package com.tvarah.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddClientRequest {

    // ── Company ───────────────────────────────────────────────────────────────
    @NotBlank(message = "Company name is required")
    private String name;

    private String industry;
    private String headquarterCity;
    private String headquarterState;
    private String headquarterCountry;
    private String companyDomain;
    private String status;
    private String sector;
    private String type;

    // ── Contact Person (stored as Draft user) ─────────────────────────────────
    @NotBlank(message = "Contact name is required")
    private String contactName;

    @NotBlank(message = "Contact role is required")
    private String contactRole;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid contact email")
    private String contactEmail;

    private String contactPhone;
}
