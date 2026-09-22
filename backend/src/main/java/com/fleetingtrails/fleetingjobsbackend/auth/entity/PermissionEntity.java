package com.fleetingtrails.fleetingjobsbackend.auth.entity;

import com.fleetingtrails.fleetingjobsbackend.auth.enums.AppModule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "permissions",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_permission_role_scope",
                columnNames = {"role_id", "module", "submodule", "action"}
        )
)
@Getter
@Setter
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @Enumerated(EnumType.STRING)
    @Column(name = "module", nullable = false, length = 64)
    private AppModule module;

    @Enumerated(EnumType.STRING)
    @Column(name = "submodule", length = 64)
    private AppModule.Submodule submodule;

    @Column(name = "action", nullable = false, length = 64)
    private String action;

    @Column(name = "permitted", nullable = false)
    private Boolean permitted = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
