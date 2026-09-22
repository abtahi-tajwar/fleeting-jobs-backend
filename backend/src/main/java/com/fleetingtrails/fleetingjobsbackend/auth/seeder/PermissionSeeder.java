package com.fleetingtrails.fleetingjobsbackend.auth.seeder;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import com.fleetingtrails.fleetingjobsbackend.auth.entity.PermissionEntity;
import com.fleetingtrails.fleetingjobsbackend.auth.entity.RoleEntity;
import com.fleetingtrails.fleetingjobsbackend.common.AppModule;
import com.fleetingtrails.fleetingjobsbackend.auth.repository.PermissionRepository;
import com.fleetingtrails.fleetingjobsbackend.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionSeeder {
    private static final String PERMISSIONS_RESOURCE = "seeds/auth/permissions.json";

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ObjectMapper objectMapper;

    public void seedPermissions() {
        ClassPathResource resource = new ClassPathResource(PERMISSIONS_RESOURCE);
        try (InputStream inputStream = resource.getInputStream()) {
            Map<String, Map<String, Object>> permissionsByRole = objectMapper.readValue(
                    inputStream,
                    new TypeReference<Map<String, Map<String, Object>>>() { }
            );

            for (Map.Entry<String, Map<String, Object>> roleEntry : permissionsByRole.entrySet()) {
                RoleEntity role = roleRepository.findByName(roleEntry.getKey())
                        .orElseThrow(() -> new IllegalStateException(
                                roleEntry.getKey() + " role not found"
                        ));

                for (Map.Entry<String, Object> moduleEntry : roleEntry.getValue().entrySet()) {
                    seedModulePermissions(role, moduleEntry.getKey(), moduleEntry.getValue());
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read " + PERMISSIONS_RESOURCE, exception);
        }
    }

    private void seedModulePermissions(RoleEntity role, String moduleKey, Object moduleValue) {
        AppModule module = AppModule.fromJsonKey(moduleKey);
        if (!(moduleValue instanceof Map<?, ?> moduleBody)) {
            throw new IllegalArgumentException(
                    "Module '" + moduleKey + "' for role '" + role.getName() + "' must be an object"
            );
        }

        for (Map.Entry<?, ?> nestedEntry : moduleBody.entrySet()) {
            String nestedKey = nestedEntry.getKey().toString();
            Object nestedValue = nestedEntry.getValue();
            Optional<AppModule.Submodule> submodule = AppModule.Submodule.of(module, nestedKey);

            if (submodule.isPresent()) {
                if (!(nestedValue instanceof Map<?, ?> actions)) {
                    throw new IllegalArgumentException(
                            "Submodule '" + nestedKey + "' under '" + module
                                    + "' for role '" + role.getName() + "' must be an object of actions"
                    );
                }
                for (Map.Entry<?, ?> actionEntry : actions.entrySet()) {
                    seedPermission(
                            role,
                            module,
                            submodule.get(),
                            actionEntry.getKey().toString(),
                            requireBoolean(actionEntry.getValue(), role.getName(), moduleKey, nestedKey)
                    );
                }
                continue;
            }

            if (nestedValue instanceof Map<?, ?>) {
                throw new IllegalArgumentException(
                        "Unknown submodule '" + nestedKey + "' under module '"
                                + module + "' for role '" + role.getName() + "'"
                );
            }

            seedPermission(
                    role,
                    module,
                    AppModule.Submodule.DEFAULT,
                    nestedKey,
                    requireBoolean(nestedValue, role.getName(), moduleKey, nestedKey)
            );
        }
    }

    private void seedPermission(
            RoleEntity role,
            AppModule module,
            AppModule.Submodule submodule,
            String action,
            boolean permitted
    ) {
        if (permissionRepository.findByRoleAndModuleAndSubmoduleAndAction(role, module, submodule, action)
                .isPresent()) {
            return;
        }

        PermissionEntity permission = new PermissionEntity();
        permission.setRole(role);
        permission.setModule(module);
        permission.setSubmodule(submodule);
        permission.setAction(action);
        permission.setPermitted(permitted);
        permissionRepository.save(permission);
    }

    private boolean requireBoolean(Object value, String roleName, String moduleKey, String actionKey) {
        if (!(value instanceof Boolean bool)) {
            throw new IllegalArgumentException(
                    "Action '" + actionKey + "' under '" + moduleKey
                            + "' for role '" + roleName + "' must be a boolean"
            );
        }
        return bool;
    }
}
