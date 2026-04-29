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
@Table(name = "job_title")
public class JobTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @Column(name = "seniority_level", length = 50)
    private String seniorityLevel;

    public JobTitle(String name, String department, String seniorityLevel) {
        this.name = name;
        this.department = department;
        this.seniorityLevel = seniorityLevel;
    }
}
