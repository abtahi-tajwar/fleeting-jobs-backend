package com.fleetingtrails.fleetingjobsbackend.auth.seeder;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import com.fleetingtrails.fleetingjobsbackend.auth.entity.RoleEntity;
import com.fleetingtrails.fleetingjobsbackend.auth.repository.RoleRepository;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ROLES_RESOURCE = "seeds/auth/roles.json";
    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;

    public void seedUser(String email, String rawOtp) {
        if (userRepository.findByEmail(email).isPresent()) return;

        RoleEntity role = roleRepository
                .findByName("SUPER_ADMIN")
                .orElseThrow(() -> new IllegalStateException("SUPER_ADMIN role not found"));

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setPassword(null);
        user.setRole(role);
        user.setOtp("0000");
        userRepository.save(user);
        System.out.println("Seeded admin user with OTP: " + email);
    }

    public void seedRoles () {
        ClassPathResource resource = new ClassPathResource(ROLES_RESOURCE);
        try (InputStream inputStream = resource.getInputStream()) {
            List<String> roles =  objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<String>>() { }
            );

            for (String roleName : roles) {
                if (roleRepository.findByName(roleName).isPresent()) continue;

                RoleEntity newRole = new RoleEntity();
                newRole.setName(roleName);
                roleRepository.save(newRole);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read " + ROLES_RESOURCE, exception);
        }
    }
}
