package com.fleetingtrails.fleetingjobsbackend.auth.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Catalog of application modules and their submodules.
 * JSON keys in {@code auth/permissions.json} must match these enum names.
 */
public enum AppModule {
    AUTH,
    USER,
    PROFILE,
    COMPANY,
    PARSER,
    JOBS,
    DOCUMENT;

    public List<Submodule> getSubmodules() {
        return Arrays.stream(Submodule.values())
                .filter(submodule -> submodule.getParentModule() == this)
                .toList();
    }

    public static AppModule fromJsonKey(String key) {
        try {
            return AppModule.valueOf(key);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unknown module in permissions.json: " + key);
        }
    }

    public enum Submodule {
        EDUCATION(AppModule.PROFILE),
        WORK_EXPERIENCE(AppModule.PROFILE),
        SKILL(AppModule.PROFILE),
        CERTIFICATION(AppModule.PROFILE),
        AWARD(AppModule.PROFILE);

        private final AppModule parentModule;

        Submodule(AppModule parentModule) {
            this.parentModule = parentModule;
        }

        public AppModule getParentModule() {
            return parentModule;
        }

        public static Optional<Submodule> of(AppModule module, String key) {
            return Arrays.stream(values())
                    .filter(submodule -> submodule.parentModule == module)
                    .filter(submodule -> submodule.name().equals(key))
                    .findFirst();
        }
    }
}
