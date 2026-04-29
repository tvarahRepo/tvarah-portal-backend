package com.tvarah.repository;

import com.tvarah.model.entity.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobTitleRepository extends JpaRepository<JobTitle, UUID> {

    Optional<JobTitle> findByNameIgnoreCase(String name);
}
