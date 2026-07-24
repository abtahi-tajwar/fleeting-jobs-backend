package com.fleetingtrails.fleetingjobsbackend.common;

import com.fleetingtrails.fleetingjobsbackend.company.seeder.CompanySeeder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeedRunner implements CommandLineRunner {
    private final CompanySeeder companySeeder;

    @Override
    public void run (String... args) throws Exception {
        companySeeder.seed();
    }

}
