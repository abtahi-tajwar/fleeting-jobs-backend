package com.fleetingtrails.fleetingjobsbackend.auth.seeder;

import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.enums.Role;
import com.fleetingtrails.fleetingjobsbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void seedUser(String email, String rawOtp) {
        if (userRepository.findByEmail(email).isPresent()) return;

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setPassword(null);
        user.setOtp(passwordEncoder.encode(rawOtp));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        System.out.println("Seeded admin user with OTP: " + email);
    }
}
