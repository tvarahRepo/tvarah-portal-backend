package com.tvarah.model.response;

import com.tvarah.model.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;
    private String keycloakUserId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String department;
    private String location;
    private String role;
    private UserStatus status;
}
