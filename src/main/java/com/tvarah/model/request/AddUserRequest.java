package com.tvarah.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddUserRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    private String phoneNumber;

    private String location;

    private String department;

    @NotBlank(message = "Role is required")
    private String role;

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setLocation(String location) { this.location = location; }
    public void setDepartment(String department) { this.department = department; }
    public void setRole(String role) { this.role = role; }
}
