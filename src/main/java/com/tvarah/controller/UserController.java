package com.tvarah.controller;

import com.tvarah.exception.UnauthorizedException;
import com.tvarah.model.request.AddUserRequest;
import com.tvarah.model.request.RoleRequest;
import com.tvarah.model.request.UpdateUserRequest;
import com.tvarah.model.response.ApiResponse;
import com.tvarah.model.response.RoleResponse;
import com.tvarah.model.response.UserResponse;
import com.tvarah.security.SecurityUtils;
import com.tvarah.service.AuthService;
import com.tvarah.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users and roles")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    // ── Role Management ──────────────────────────────────────────────────────

    @GetMapping("/roles")
    @PreAuthorize("hasRole('SITE_ADMIN')")
    @Operation(summary = "Get all roles", description = "Returns all realm roles defined in Keycloak, excluding internal system roles.")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllRoles()));
    }

    @PostMapping("/roles")
    @PreAuthorize("hasRole('SITE_ADMIN')")
    @Operation(summary = "Create a role", description = "Creates a new realm role in Keycloak.")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Role created successfully", userService.createRole(request)));
    }

    @DeleteMapping("/roles/{roleName}")
    @PreAuthorize("hasRole('SITE_ADMIN')")
    @Operation(summary = "Delete a role", description = "Deletes a realm role from Keycloak by name.")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable String roleName) {
        userService.deleteRole(roleName);
        return ResponseEntity.ok(ApiResponse.success("Role deleted successfully", null));
    }

    // ── User Management ──────────────────────────────────────────────────────

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Returns the authenticated user's profile from the database, including their assigned roles.")
    public ResponseEntity<ApiResponse<UserResponse>> getMe() {
        String keycloakUserId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new UnauthorizedException("User not authenticated"));
        return ResponseEntity.ok(ApiResponse.success(userService.getMe(keycloakUserId)));
    }

    @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update profile", description = "Updates the authenticated user's profile details and/or profile picture. All fields are optional.")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @RequestPart(value = "data", required = false) com.tvarah.model.request.UpdateProfileRequest data,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        String keycloakUserId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new UnauthorizedException("User not authenticated"));
        if (data == null) data = new com.tvarah.model.request.UpdateProfileRequest();
        UserResponse updated = userService.updateProfile(keycloakUserId, data, avatar);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updated));
    }

    @GetMapping("/{keycloakUserId}/avatar")
    @Operation(summary = "Get profile picture", description = "Streams the user's profile picture. This endpoint is publicly accessible.")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String keycloakUserId) {
        byte[] image = userService.getAvatar(keycloakUserId);
        String contentType = userService.getAvatarContentType(keycloakUserId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(image);
    }

    @GetMapping
    @PreAuthorize("hasRole('SITE_ADMIN')")
    @Operation(summary = "Get all users", description = "Returns all platform users, excluding Draft users.")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
    }

    @PostMapping("/draft")
    @PreAuthorize("hasRole('SITE_ADMIN')")
    @Operation(summary = "Create a draft user", description = "Creates a user record in the DB only — no Keycloak account, no email. Status is Draft.")
    public ResponseEntity<ApiResponse<UserResponse>> createDraftUser(@Valid @RequestBody AddUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Draft user created", userService.createDraftUser(request)));
    }

    @PostMapping("/{userId}/invite")
    @PreAuthorize("hasRole('SITE_ADMIN')")
    @Operation(summary = "Invite a draft user", description = "Creates a Keycloak account for a Draft user, sends an invite email, and sets status to Pending.")
    public ResponseEntity<ApiResponse<Void>> inviteUser(@PathVariable UUID userId) {
        authService.inviteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Invite sent successfully", null));
    }

    @PostMapping("/{userId}/roles/{roleName}")
    @PreAuthorize("hasRole('SITE_ADMIN')")
    @Operation(summary = "Assign role to user", description = "Assigns a realm role to an existing Keycloak user.")
    public ResponseEntity<ApiResponse<Void>> assignRole(@PathVariable String userId,
                                                        @PathVariable String roleName) {
        userService.assignRole(userId, roleName);
        return ResponseEntity.ok(ApiResponse.success("Role assigned successfully", null));
    }

    @DeleteMapping("/{userId}/roles/{roleName}")
    @PreAuthorize("hasRole('SITE_ADMIN')")
    @Operation(summary = "Remove role from user", description = "Removes a realm role from a Keycloak user.")
    public ResponseEntity<ApiResponse<Void>> removeRole(@PathVariable String userId,
                                                        @PathVariable String roleName) {
        userService.removeRole(userId, roleName);
        return ResponseEntity.ok(ApiResponse.success("Role removed successfully", null));
    }

    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasRole('SITE_ADMIN')")
    @Operation(summary = "Enable or disable a user", description = "Toggles the enabled state of a Keycloak user.")
    public ResponseEntity<ApiResponse<Void>> updateUserStatus(@PathVariable String userId,
                                                              @RequestParam boolean enabled) {
        userService.updateUserStatus(userId, enabled);
        String msg = enabled ? "User enabled successfully" : "User disabled successfully";
        return ResponseEntity.ok(ApiResponse.success(msg, null));
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasRole('SITE_ADMIN')")
    @Operation(summary = "Update a user", description = "Updates a user's profile fields and role. All fields are optional.")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable String userId,
                                                                @RequestBody UpdateUserRequest request) {
        UserResponse updated = userService.updateUser(userId, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updated));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('SITE_ADMIN')")
    @Operation(summary = "Delete a user", description = "Permanently deletes a user from Keycloak.")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
}
