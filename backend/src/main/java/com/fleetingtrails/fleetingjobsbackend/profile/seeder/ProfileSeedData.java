package com.fleetingtrails.fleetingjobsbackend.profile.seeder;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProfileSeedData {
    private String email;
    private UserSeed user;
    private List<EducationSeed> educations = new ArrayList<>();
    private List<WorkExperienceSeed> workExperiences = new ArrayList<>();
    private List<SkillSeed> skills = new ArrayList<>();
    private List<AwardSeed> awards = new ArrayList<>();
    private List<CertificationSeed> certifications = new ArrayList<>();

    @Data
    public static class UserSeed {
        private String firstName;
        private String lastName;
        private String phone;
        private String linkedin;
        private String github;
        private String portfolioWebsite;
        private String city;
        private String province;
        private String country;
        private String summary;
    }

    @Data
    public static class EducationSeed {
        private String institution;
        private String degree;
        private String major;
        private String gpa;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean graduated;
    }

    @Data
    public static class WorkExperienceSeed {
        private String company;
        private String position;
        private String location;
        private String employmentType;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean currentlyWorking;
        private String description;
    }

    @Data
    public static class SkillSeed {
        private String name;
        private String category;
        private Integer strength;
        private Double yearsExperience;
        private String notes;
    }

    @Data
    public static class AwardSeed {
        private String title;
        private String organization;
        private LocalDate date;
        private String description;
    }

    @Data
    public static class CertificationSeed {
        private String name;
        private String issuer;
        private LocalDate issuedDate;
        private LocalDate expiryDate;
        private String credentialId;
        private String verificationUrl;
    }
}
