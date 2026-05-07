package com.tvarah.controller;

import com.tvarah.exception.UnauthorizedException;
import com.tvarah.model.request.RoleRequest;
import com.tvarah.model.response.ApiResponse;
import com.tvarah.model.response.RoleResponse;
import com.tvarah.model.response.UserResponse;
import com.tvarah.security.SecurityUtils;
import com.tvarah.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users and roles")
public class UserController {

    private final UserService userService;

    // ── Role Management ──────────────────────────────────────────────────────

    @GetMapping("/roles")
    @Operation(summary = "Get all roles", description = "Returns all realm roles defined in Keycloak, excluding internal system roles.")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllRoles()));
    }

    @PostMapping("/roles")
    @Operation(summary = "Create a role", description = "Creates a new realm role in Keycloak.")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Role created successfully", userService.createRole(request)));
    }

    @DeleteMapping("/roles/{roleName}")
    @Operation(summary = "Delete a role", description = "Deletes a realm role from Keycloak by name.")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable String roleName) {
        userService.deleteRole(roleName);
        return ResponseEntity.ok(ApiResponse.success("Role deleted successfully", null));
    }

    // ── User Management ──────────────────────────────────────────────────────

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get current user", description = "Returns the authenticated user's profile from the database, including their assigned roles.")
    public ResponseEntity<ApiResponse<UserResponse>> getMe() {
        String keycloakUserId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new UnauthorizedException("User not authenticated"));
        return ResponseEntity.ok(ApiResponse.success(userService.getMe(keycloakUserId)));
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns all Keycloak users along with their assigned realm roles.")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
    }

    @PostMapping("/{userId}/roles/{roleName}")
    @Operation(summary = "Assign role to user", description = "Assigns a realm role to an existing Keycloak user.")
    public ResponseEntity<ApiResponse<Void>> assignRole(@PathVariable String userId,
                                                        @PathVariable String roleName) {
        userService.assignRole(userId, roleName);
        return ResponseEntity.ok(ApiResponse.success("Role assigned successfully", null));
    }

    @DeleteMapping("/{userId}/roles/{roleName}")
    @Operation(summary = "Remove role from user", description = "Removes a realm role from a Keycloak user.")
    public ResponseEntity<ApiResponse<Void>> removeRole(@PathVariable String userId,
                                                        @PathVariable String roleName) {
        userService.removeRole(userId, roleName);
        return ResponseEntity.ok(ApiResponse.success("Role removed successfully", null));
    }

    @PatchMapping("/{userId}/status")
    @Operation(summary = "Enable or disable a user", description = "Toggles the enabled state of a Keycloak user.")
    public ResponseEntity<ApiResponse<Void>> updateUserStatus(@PathVariable String userId,
                                                              @RequestParam boolean enabled) {
        userService.updateUserStatus(userId, enabled);
        String msg = enabled ? "User enabled successfully" : "User disabled successfully";
        return ResponseEntity.ok(ApiResponse.success(msg, null));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete a user", description = "Permanently deletes a user from Keycloak.")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
}
