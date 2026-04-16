package com.tvarah.mapper;

import com.tvarah.model.entity.JobDescription;
import com.tvarah.model.response.JobDescriptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class JobDescriptionMapper {

    public JobDescriptionResponse toResponse(
            JobDescription entity,
            Map<UUID, String> companyNames,
            Map<UUID, String> jobTitleNames,
            Map<UUID, String> skillNames) {

        if (entity == null) return null;

        return JobDescriptionResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .company(companyNames.get(entity.getCompanyId()))
                .jobTitle(jobTitleNames.get(entity.getJobTitleId()))
                .jobType(entity.getJobType())
                .jobMode(entity.getJobMode())
                .jobLevel(entity.getJobLevel())
                .jobDescriptionText(entity.getJobDescriptionText())
                .summaryResponsibilities(entity.getSummaryResponsibilities())
                .experienceMinYrs(entity.getExperienceMinYrs())
                .experienceMaxYrs(entity.getExperienceMaxYrs())
                .requiredSkills(resolveSkillNames(entity.getRequiredSkills(), skillNames))
                .goodToHaveSkills(resolveSkillNames(entity.getGoodToHaveSkills(), skillNames))
                .salaryMin(entity.getSalaryMin())
                .salaryMax(entity.getSalaryMax())
                .totalPositions(entity.getTotalPositions())
                .totalPositionsSelected(entity.getTotalPositionsSelected())
                .totalRounds(entity.getTotalRounds())
                .status(entity.getStatus())
                .closedOn(entity.getClosedOn())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    private List<String> resolveSkillNames(List<UUID> skillIds, Map<UUID, String> skillNames) {
        if (skillIds == null || skillIds.isEmpty()) return List.of();
        return skillIds.stream()
                .map(skillNames::get)
                .filter(name -> name != null)
                .toList();
    }

    public List<JobDescriptionResponse> toResponseList(
            List<JobDescription> entities,
            Map<UUID, String> companyNames,
            Map<UUID, String> jobTitleNames,
            Map<UUID, String> skillNames) {
        return entities.stream()
                .map(e -> toResponse(e, companyNames, jobTitleNames, skillNames))
                .toList();
    }
}
