package com.tvarah.service.impl;

import com.tvarah.exception.BadRequestException;
import com.tvarah.exception.ResourceNotFoundException;
import com.tvarah.model.entity.User;
import com.tvarah.model.enums.UserStatus;
import com.tvarah.model.request.RoleRequest;
import com.tvarah.model.response.RoleResponse;
import com.tvarah.model.response.UserResponse;
import com.tvarah.repository.UserRepository;
import com.tvarah.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.ws.rs.ClientErrorException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Set<String> SYSTEM_ROLES = Set.of(
            "offline_access", "uma_authorization", "default-roles-tvarah", "uma_protection"
    );

    private final Keycloak keycloakAdmin;
    private final UserRepository userRepository;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${app.base-url}")
    private String baseUrl;

    // ── Role Management ──────────────────────────────────────────────────────

    @Override
    public List<RoleResponse> getAllRoles() {
        return keycloakAdmin.realm(realm).roles().list().stream()
                .filter(r -> !SYSTEM_ROLES.contains(r.getName()))
                .map(this::toRoleResponse)
                .toList();
    }

    @Override
    public RoleResponse createRole(RoleRequest request) {
        RoleRepresentation role = new RoleRepresentation();
        role.setName(request.getName().trim());
        role.setDescription(request.getDescription());

        try {
            keycloakAdmin.realm(realm).roles().create(role);
            log.info("Created Keycloak role: {}", request.getName());
        } catch (ClientErrorException e) {
            if (e.getResponse().getStatus() == 409) {
                throw new BadRequestException("Role '" + request.getName() + "' already exists");
            }
            log.error("Keycloak role creation failed with status {}: {}", e.getResponse().getStatus(), e.getMessage());
            throw new BadRequestException("Failed to create role");
        } catch (Exception e) {
            log.error("Unexpected error creating Keycloak role: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to create role");
        }

        RoleRepresentation created = keycloakAdmin.realm(realm).roles().get(request.getName().trim()).toRepresentation();
        return toRoleResponse(created);
    }

    @Override
    public void deleteRole(String roleName) {
        try {
            keycloakAdmin.realm(realm).roles().get(roleName).remove();
            log.info("Deleted Keycloak role: {}", roleName);
        } catch (Exception e) {
            log.error("Failed to delete role '{}': {}", roleName, e.getMessage());
            throw new ResourceNotFoundException("Role not found: " + roleName);
        }
    }

    // ── User Management ──────────────────────────────────────────────────────

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse getMe(String keycloakUserId) {
        User user = userRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + keycloakUserId));
        return toUserResponse(user);
    }

    @Override
    public void saveUser(String keycloakUserId, String firstName, String lastName, String email,
                         String phoneNumber, String location, String department, String role) {
        User user = User.builder()
                .keycloakUserId(keycloakUserId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .location(location)
                .department(department)
                .role(role)
                .status(UserStatus.PENDING)
                .build();
        userRepository.save(user);
        log.info("Saved user to DB with status PENDING: {}", email);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        return toUserResponse(user);
    }

    @Override
    public void activateUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        log.info("User status set to ACTIVE for: {}", email);
    }

    @Override
    public void assignRole(String keycloakUserId, String roleName) {
        RoleRepresentation role = getRole(roleName);
        try {
            keycloakAdmin.realm(realm).users().get(keycloakUserId)
                    .roles().realmLevel().add(List.of(role));
            log.info("Assigned role '{}' to user '{}'", roleName, keycloakUserId);
        } catch (Exception e) {
            log.error("Failed to assign role '{}' to user '{}'", roleName, keycloakUserId, e);
            throw new BadRequestException("Failed to assign role to user");
        }
    }

    @Override
    public void removeRole(String keycloakUserId, String roleName) {
        RoleRepresentation role = getRole(roleName);
        try {
            keycloakAdmin.realm(realm).users().get(keycloakUserId)
                    .roles().realmLevel().remove(List.of(role));
            log.info("Removed role '{}' from user '{}'", roleName, keycloakUserId);
        } catch (Exception e) {
            log.error("Failed to remove role '{}' from user '{}'", roleName, keycloakUserId, e);
            throw new BadRequestException("Failed to remove role from user");
        }
    }

    @Override
    public void updateUserStatus(String keycloakUserId, boolean enabled) {
        // Update Keycloak
        try {
            org.keycloak.representations.idm.UserRepresentation kcUser = new org.keycloak.representations.idm.UserRepresentation();
            kcUser.setEnabled(enabled);
            keycloakAdmin.realm(realm).users().get(keycloakUserId).update(kcUser);
        } catch (Exception e) {
            log.error("Failed to update Keycloak status for user '{}': {}", keycloakUserId, e.getMessage());
            throw new BadRequestException("Failed to update user status");
        }

        // Update DB
        User user = userRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + keycloakUserId));
        user.setStatus(enabled ? UserStatus.ACTIVE : UserStatus.INACTIVE);
        userRepository.save(user);
        log.info("User '{}' status set to {}", keycloakUserId, user.getStatus());
    }

    @Override
    public void deleteUser(String keycloakUserId) {
        // Delete from Keycloak
        try {
            keycloakAdmin.realm(realm).users().get(keycloakUserId).remove();
            log.info("Deleted Keycloak user: {}", keycloakUserId);
        } catch (Exception e) {
            log.error("Failed to delete Keycloak user '{}': {}", keycloakUserId, e.getMessage());
            throw new ResourceNotFoundException("User not found: " + keycloakUserId);
        }

        // Delete from DB
        userRepository.findByKeycloakUserId(keycloakUserId).ifPresent(u -> {
            userRepository.delete(u);
            log.info("Deleted user from DB: {}", keycloakUserId);
        });
    }

    @Override
    public UserResponse updateProfile(String keycloakUserId, com.tvarah.model.request.UpdateProfileRequest request, MultipartFile avatar) {
        User user = userRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + keycloakUserId));

        if (request.getFirstName() != null)   user.setFirstName(request.getFirstName());
        if (request.getLastName() != null)    user.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getLocation() != null)    user.setLocation(request.getLocation());
        if (request.getDepartment() != null)  user.setDepartment(request.getDepartment());

        if (avatar != null && !avatar.isEmpty()) {
            try {
                user.setAvatar(avatar.getBytes());
                user.setAvatarContentType(avatar.getContentType());
            } catch (IOException e) {
                log.error("Failed to read avatar file for user: {}", keycloakUserId, e);
                throw new BadRequestException("Failed to process avatar file");
            }
        }

        userRepository.save(user);
        log.info("Profile updated for user: {}", keycloakUserId);
        return toUserResponse(user);
    }

    @Override
    public byte[] getAvatar(String keycloakUserId) {
        User user = userRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + keycloakUserId));
        if (user.getAvatar() == null) {
            throw new ResourceNotFoundException("No avatar found for user: " + keycloakUserId);
        }
        return user.getAvatar();
    }

    @Override
    public String getAvatarContentType(String keycloakUserId) {
        User user = userRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + keycloakUserId));
        return user.getAvatarContentType() != null ? user.getAvatarContentType() : "image/jpeg";
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .keycloakUserId(user.getKeycloakUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .department(user.getDepartment())
                .location(user.getLocation())
                .role(user.getRole())
                .status(user.getStatus())
                .avatarUrl(user.getAvatar() != null ? baseUrl + "/users/" + user.getKeycloakUserId() + "/avatar" : null)
                .build();
    }

    private RoleRepresentation getRole(String roleName) {
        try {
            return keycloakAdmin.realm(realm).roles().get(roleName).toRepresentation();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Role not found: " + roleName);
        }
    }

    private RoleResponse toRoleResponse(RoleRepresentation r) {
        return RoleResponse.builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .build();
    }
}
