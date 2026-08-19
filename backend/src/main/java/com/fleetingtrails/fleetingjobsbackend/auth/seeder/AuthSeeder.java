package com.fleetingtrails.fleetingjobsbackend.auth.seeder;

import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthSeeder {

    private final UserRepository userRepository;

    public void seedUser(String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) return;

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setPassword(new BCryptPasswordEncoder().encode(rawPassword));
        user.setRole(com.fleetingtrails.fleetingjobsbackend.user.enums.Role.ADMIN);
        userRepository.save(user);
        System.out.println("Seeded admin user: " + email);
    }
}
