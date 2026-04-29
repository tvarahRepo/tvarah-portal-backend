package com.tvarah.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "category", nullable = false, length = 200)
    private String category;

    @Column(name = "tier", length = 50)
    private String tier;

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @Column(name = "is_technical", nullable = false)
    private boolean isTechnical = true;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    public Skill(String name, String category, String department, boolean isTechnical) {
        this.name = name;
        this.category = category;
        this.department = department;
        this.isTechnical = isTechnical;
        this.isVerified = false;
    }
}
