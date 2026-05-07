package com.tvarah.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RoleRequest {

    @NotBlank(message = "Role name is required")
    private String name;

    private String description;
}
