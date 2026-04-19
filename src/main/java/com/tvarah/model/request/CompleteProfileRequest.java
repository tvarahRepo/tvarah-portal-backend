package com.tvarah.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CompleteProfileRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;
}
