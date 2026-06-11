package com.tvarah.repository;

import com.tvarah.model.entity.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyUserRepository extends JpaRepository<CompanyUser, UUID> {

    List<CompanyUser> findByUserId(UUID userId);

    List<CompanyUser> findByCompanyId(UUID companyId);
}
