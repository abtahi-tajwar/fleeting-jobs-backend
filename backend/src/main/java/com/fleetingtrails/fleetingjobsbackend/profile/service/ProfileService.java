package com.fleetingtrails.fleetingjobsbackend.profile.service;

import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.repository.AwardRepository;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.repository.CertificationRepository;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.repository.EducationRepository;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.repository.SkillRepository;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.repository.WorkExperienceRepository;
import com.fleetingtrails.fleetingjobsbackend.profile.dto.ProfileGetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final SkillRepository skillRepository;
    private final EducationRepository educationRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final CertificationRepository certificationRepository;
    private final AwardRepository awardRepository;

    public ProfileGetResponseDto getProfile (long userId) {
        ProfileGetResponseDto data = new ProfileGetResponseDto();
        data.setSkills(skillRepository.findByUserId(userId));
        data.setCertifications(certificationRepository.findByUserId(userId));
        data.setAwards(awardRepository.findByUserId(userId));
        data.setEducations(educationRepository.findByUserId(userId));
        data.setWorkExperiences(workExperienceRepository.findByUserId(userId));
        return data;
    }
}
