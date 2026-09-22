package com.fleetingtrails.fleetingjobsbackend.common.seeder;

import com.fleetingtrails.fleetingjobsbackend.auth.seeder.AuthSeeder;
import com.fleetingtrails.fleetingjobsbackend.auth.seeder.PermissionSeeder;
import com.fleetingtrails.fleetingjobsbackend.profile.seeder.ProfileSeeder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final AuthSeeder authSeeder;
    private final PermissionSeeder permissionSeeder;
    private final ProfileSeeder profileSeeder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Seeding database...");
        authSeeder.seedRoles();
        permissionSeeder.seedPermissions();
        authSeeder.seedUser("admin@test.com", "0000");
        authSeeder.seedUser(
                "abtahitajwar@gmail.com",
                "0000",
                "SUBSCRIBER",
                "Abtahi",
                "Tajwar"
        );
        profileSeeder.seedDemoProfile();
        System.out.println("Seeding complete.");
    }
}
