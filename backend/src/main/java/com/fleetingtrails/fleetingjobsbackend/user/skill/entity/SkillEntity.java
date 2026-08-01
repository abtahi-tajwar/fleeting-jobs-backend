package com.fleetingtrails.fleetingjobsbackend.user.skill.entity;

import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String name;

    private String category;

    @Column(nullable = false)
    private Integer strength; // Valid  1-10 in DTO

    @Column(name = "years_experience")
    private Double yearsExperience;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
