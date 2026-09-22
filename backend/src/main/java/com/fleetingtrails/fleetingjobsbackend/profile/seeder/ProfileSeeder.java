package com.fleetingtrails.fleetingjobsbackend.profile.seeder;

import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.entity.AwardEntity;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.repository.AwardRepository;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.entity.CertificationEntity;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.repository.CertificationRepository;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.entity.EducationEntity;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.repository.EducationRepository;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.entity.SkillEntity;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.repository.SkillRepository;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.entity.WorkExperienceEntity;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.repository.WorkExperienceRepository;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ProfileSeeder {
    private static final String PROFILE_RESOURCE = "seeds/profile/demo-profile.json";

    private final UserRepository userRepository;
    private final EducationRepository educationRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final SkillRepository skillRepository;
    private final AwardRepository awardRepository;
    private final CertificationRepository certificationRepository;
    private final ObjectMapper objectMapper;

    public void seedDemoProfile() {
        ClassPathResource resource = new ClassPathResource(PROFILE_RESOURCE);
        try (InputStream inputStream = resource.getInputStream()) {
            ProfileSeedData seed = objectMapper.readValue(
                    inputStream,
                    new TypeReference<ProfileSeedData>() { }
            );

            UserEntity user = userRepository.findByEmail(seed.getEmail())
                    .orElseThrow(() -> new IllegalStateException(
                            seed.getEmail() + " user not found"
                    ));

            seedUserProfile(user, seed.getUser());
            seedEducations(user, seed);
            seedWorkExperiences(user, seed);
            seedSkills(user, seed);
            seedAwards(user, seed);
            seedCertifications(user, seed);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read " + PROFILE_RESOURCE, exception);
        }
    }

    private void seedUserProfile(UserEntity user, ProfileSeedData.UserSeed header) {
        if (header == null) {
            return;
        }

        user.setFirstName(header.getFirstName());
        user.setLastName(header.getLastName());
        user.setPhone(header.getPhone());
        user.setLinkedin(header.getLinkedin());
        user.setGithub(header.getGithub());
        user.setPortfolioWebsite(header.getPortfolioWebsite());
        user.setCity(header.getCity());
        user.setProvince(header.getProvince());
        user.setCountry(header.getCountry());
        user.setSummary(header.getSummary());
        userRepository.save(user);
    }

    private void seedEducations(UserEntity user, ProfileSeedData seed) {
        if (!educationRepository.findByUserId(user.getId()).isEmpty()) {
            return;
        }

        for (ProfileSeedData.EducationSeed item : seed.getEducations()) {
            EducationEntity education = new EducationEntity();
            education.setUser(user);
            education.setInstitution(item.getInstitution());
            education.setDegree(item.getDegree());
            education.setMajor(item.getMajor());
            education.setGpa(item.getGpa());
            education.setStartDate(item.getStartDate());
            education.setEndDate(item.getEndDate());
            education.setGraduated(item.getGraduated());
            educationRepository.save(education);
        }
    }

    private void seedWorkExperiences(UserEntity user, ProfileSeedData seed) {
        if (!workExperienceRepository.findByUserId(user.getId()).isEmpty()) {
            return;
        }

        for (ProfileSeedData.WorkExperienceSeed item : seed.getWorkExperiences()) {
            WorkExperienceEntity experience = new WorkExperienceEntity();
            experience.setUser(user);
            experience.setCompany(item.getCompany());
            experience.setPosition(item.getPosition());
            experience.setLocation(item.getLocation());
            experience.setEmploymentType(item.getEmploymentType());
            experience.setStartDate(item.getStartDate());
            experience.setEndDate(item.getEndDate());
            experience.setCurrentlyWorking(item.getCurrentlyWorking());
            experience.setDescription(item.getDescription());
            workExperienceRepository.save(experience);
        }
    }

    private void seedSkills(UserEntity user, ProfileSeedData seed) {
        if (!skillRepository.findByUserId(user.getId()).isEmpty()) {
            return;
        }

        for (ProfileSeedData.SkillSeed item : seed.getSkills()) {
            SkillEntity skill = new SkillEntity();
            skill.setUser(user);
            skill.setName(item.getName());
            skill.setCategory(item.getCategory());
            skill.setStrength(item.getStrength());
            skill.setYearsExperience(item.getYearsExperience());
            skill.setNotes(item.getNotes());
            skillRepository.save(skill);
        }
    }

    private void seedAwards(UserEntity user, ProfileSeedData seed) {
        if (!awardRepository.findByUserId(user.getId()).isEmpty()) {
            return;
        }

        for (ProfileSeedData.AwardSeed item : seed.getAwards()) {
            AwardEntity award = new AwardEntity();
            award.setUser(user);
            award.setTitle(item.getTitle());
            award.setOrganization(item.getOrganization());
            award.setDate(item.getDate());
            award.setDescription(item.getDescription());
            awardRepository.save(award);
        }
    }

    private void seedCertifications(UserEntity user, ProfileSeedData seed) {
        if (!certificationRepository.findByUserId(user.getId()).isEmpty()) {
            return;
        }

        for (ProfileSeedData.CertificationSeed item : seed.getCertifications()) {
            CertificationEntity certification = new CertificationEntity();
            certification.setUser(user);
            certification.setName(item.getName());
            certification.setIssuer(item.getIssuer());
            certification.setIssuedDate(item.getIssuedDate());
            certification.setExpiryDate(item.getExpiryDate());
            certification.setCredentialId(item.getCredentialId());
            certification.setVerificationUrl(item.getVerificationUrl());
            certificationRepository.save(certification);
        }
    }
}
