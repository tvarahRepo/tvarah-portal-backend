package com.tvarah.model.entity;

import com.tvarah.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "keycloak_user_id", unique = true, length = 36)
    private String keycloakUserId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", length = 150)
    private String firstName;

    @Column(name = "last_name", length = 150)
    private String lastName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "status", length = 100)
    private UserStatus status;

    @CreatedDate
    @Column(name = "created_on", updatable = false, nullable = false)
    private Instant createdOn;

    @LastModifiedDate
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}
