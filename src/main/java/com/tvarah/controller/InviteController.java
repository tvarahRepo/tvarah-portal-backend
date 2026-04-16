package com.tvarah.controller;

import com.tvarah.model.request.InviteRequest;
import com.tvarah.model.response.ApiResponse;
import com.tvarah.service.InviteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invite")
@RequiredArgsConstructor
@Tag(name = "Invite", description = "APIs for inviting users to the portal")
public class InviteController {

    private final InviteService inviteService;

    @PostMapping
    @Operation(
            summary = "Invite a user",
            description = "Creates a Keycloak account with a temporary password and sends an invitation email to the provided address."
    )
    public ResponseEntity<ApiResponse<Void>> invite(@Valid @RequestBody InviteRequest request) {
        inviteService.inviteUser(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Invitation sent successfully", null));
    }
}
