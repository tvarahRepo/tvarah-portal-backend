package com.tvarah.service;

import com.tvarah.model.request.AddUserRequest;
import com.tvarah.model.request.RoleRequest;
import com.tvarah.model.request.UpdateUserRequest;
import com.tvarah.model.response.RoleResponse;
import com.tvarah.model.response.UserResponse;
import com.tvarah.model.request.UpdateProfileRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<RoleResponse> getAllRoles();

    RoleResponse createRole(RoleRequest request);

    void deleteRole(String roleName);

    List<UserResponse> getAllUsers();

    UserResponse createDraftUser(AddUserRequest request);

    com.tvarah.model.entity.User getUserEntityById(UUID userId);

    void promoteFromDraft(UUID userId, String keycloakId);

    UserResponse getMe(String keycloakUserId);

    UserResponse getUserByEmail(String email);

    void saveUser(String keycloakUserId, String firstName, String lastName, String email, String phoneNumber, String location, String department, String role);

    void activateUserByEmail(String email);

    void assignRole(String keycloakUserId, String roleName);

    void removeRole(String keycloakUserId, String roleName);

    void updateUserStatus(String keycloakUserId, boolean enabled);

    void deleteUser(String keycloakUserId);

    UserResponse updateProfile(String keycloakUserId, UpdateProfileRequest request, MultipartFile avatar);

    UserResponse updateUser(String keycloakUserId, UpdateUserRequest request);

    byte[] getAvatar(String keycloakUserId);

    String getAvatarContentType(String keycloakUserId);
}
