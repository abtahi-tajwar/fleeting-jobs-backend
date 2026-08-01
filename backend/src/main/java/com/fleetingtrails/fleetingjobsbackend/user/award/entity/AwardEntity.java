package com.fleetingtrails.fleetingjobsbackend.user.award.entity;

import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "awards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String title;

    private String organization;

    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String description;
}
