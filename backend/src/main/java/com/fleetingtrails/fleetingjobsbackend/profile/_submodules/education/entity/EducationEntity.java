package com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "educations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    @Column(nullable = false)
    private String institution;

    @Column(nullable = false)
    private String degree;

    private String major;

    private String gpa;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private Boolean graduated;
}