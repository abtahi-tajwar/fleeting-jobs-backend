package com.fleetingtrails.fleetingjobsbackend.company.entity;

import com.fleetingtrails.fleetingjobsbackend.jobs.entity.JobEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "listing_url", nullable = false)
    private String listingUrl;
    @Column(name = "single_page_url_template", nullable = true)
    private String singlePageUrlTemplate;
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobEntity> jobs = new ArrayList<>();
    @UpdateTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @CreationTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
