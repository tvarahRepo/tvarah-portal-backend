package com.tvarah.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String location;
    private String department;
    private String role;
}
