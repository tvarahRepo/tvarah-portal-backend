package com.tvarah.mapper;

import com.tvarah.model.entity.Candidate;
import com.tvarah.model.entity.CandidateNoticePeriod;
import com.tvarah.model.entity.CandidatePreference;
import com.tvarah.model.entity.CandidateScore;
import com.tvarah.model.response.CandidateResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CandidateMapper {

    public CandidateResponse toResponse(
            Candidate entity,
            Map<UUID, String> jobTitleNames,
            Map<UUID, String> designationNames,
            Map<UUID, String> companyNames,
            Map<UUID, CandidatePreference> preferenceMap,
            Map<UUID, CandidateNoticePeriod> noticePeriodMap,
            Map<UUID, CandidateScore> scoreMap) {

        if (entity == null) return null;

        CandidatePreference pref = preferenceMap.get(entity.getId());
        CandidateNoticePeriod notice = noticePeriodMap.get(entity.getId());
        CandidateScore score = scoreMap.get(entity.getId());

        return CandidateResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .lastName(entity.getLastName())
                .gender(entity.getGender())
                .primaryEmail(entity.getPrimaryEmail())
                .secondaryEmail(entity.getSecondaryEmail())
                .primaryPhoneNumber(entity.getPrimaryPhoneNumber())
                .secondaryPhoneNumber(entity.getSecondaryPhoneNumber())
                .address(entity.getAddress())
                .city(entity.getCity())
                .country(entity.getCountry())
                .avatarColor(entity.getAvatarColor())
                .starred(entity.getStarred())
                .status(entity.getStatus())
                .jobTitle(jobTitleNames.get(entity.getJobTitleId()))
                .designation(designationNames.get(entity.getDesignationId()))
                .currentCompany(companyNames.get(entity.getCurrentCompanyId()))
                .totalExperience(entity.getTotalExperience())
                .relevantExperience(entity.getRelevantExperience())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                // Work preference
                .workModePreference(pref != null ? pref.getWorkModePreference() : null)
                .canRelocate(pref != null ? pref.getCanRelocate() : null)
                .workAuthorization(pref != null ? pref.getWorkAuthorization() : null)
                .currentCtc(pref != null ? pref.getCurrentCtc() : null)
                .expectedCtcMin(pref != null ? pref.getExpectedCtcMin() : null)
                .expectedCtcMax(pref != null ? pref.getExpectedCtcMax() : null)
                .ctcCurrency(pref != null ? pref.getCtcCurrency() : null)
                // Notice period
                .isInNoticePeriod(notice != null ? notice.getIsInNoticePeriod() : null)
                .noticePeriodDuration(notice != null ? notice.getNoticePeriodDuration() : null)
                .lastWorkingDate(notice != null ? notice.getLastWorkingDate() : null)
                .earliestJoiningDate(notice != null ? notice.getEarliestJoiningDate() : null)
                // Resume score
                .resumeScore(score != null ? score.getScore() : null)
                .resumeMaxScore(score != null ? score.getMaxScore() : null)
                .strength(score != null ? score.getStrength() : null)
                .dropFlag(score != null ? score.getDropFlag() : null)
                .fraudRisk(score != null ? score.getFraudRisk() : null)
                .build();
    }

    public List<CandidateResponse> toResponseList(
            List<Candidate> entities,
            Map<UUID, String> jobTitleNames,
            Map<UUID, String> designationNames,
            Map<UUID, String> companyNames,
            Map<UUID, CandidatePreference> preferenceMap,
            Map<UUID, CandidateNoticePeriod> noticePeriodMap,
            Map<UUID, CandidateScore> scoreMap) {

        return entities.stream()
                .map(e -> toResponse(e, jobTitleNames, designationNames, companyNames,
                        preferenceMap, noticePeriodMap, scoreMap))
                .toList();
    }
}
