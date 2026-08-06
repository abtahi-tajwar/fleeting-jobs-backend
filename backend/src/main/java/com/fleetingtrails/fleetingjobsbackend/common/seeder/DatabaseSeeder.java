package com.fleetingtrails.fleetingjobsbackend.common.seeder;

import com.fleetingtrails.fleetingjobsbackend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final AuthService authService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Seeding database...");
        // Seeds an Admin user: admin@test.com / password123
        authService.seedUser("admin@test.com", "password123");

        // For regular use calling :
        // authService.seedUser("user@test.com", "password123");
        System.out.println("Seeding complete.");
    }
}
