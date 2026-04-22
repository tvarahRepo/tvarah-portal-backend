package com.tvarah.repository;

import com.tvarah.model.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DesignationRepository extends JpaRepository<Designation, UUID> {
}
