package com.fleetingtrails.fleetingjobsbackend.auth.repository;

import com.fleetingtrails.fleetingjobsbackend.auth.entity.PermissionEntity;
import com.fleetingtrails.fleetingjobsbackend.auth.entity.RoleEntity;
import com.fleetingtrails.fleetingjobsbackend.common.AppModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    List<PermissionEntity> findByRole(RoleEntity role);

    Optional<PermissionEntity> findByRoleAndModuleAndSubmoduleAndAction(
            RoleEntity role,
            AppModule module,
            AppModule.Submodule submodule,
            String action
    );
}
