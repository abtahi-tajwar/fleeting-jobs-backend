package com.fleetingtrails.fleetingjobsbackend.profile.dto;

import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.entity.AwardEntity;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.entity.CertificationEntity;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.entity.EducationEntity;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.entity.SkillEntity;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.entity.WorkExperienceEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileGetResponseDto {
    List<SkillEntity> skills;
    List<WorkExperienceEntity> workExperiences;
    List<EducationEntity> educations;
    List<CertificationEntity> certifications;
    List<AwardEntity> awards;
}
