package com.fleetingtrails.fleetingjobsbackend.auth.service;

import com.fleetingtrails.fleetingjobsbackend.auth.dto.AuthResponseDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.ConfirmEmailRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.GoogleSignupRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.LinkedInSignupRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.LoginRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.SetPasswordRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.SignupRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.SignupResponseDto;
import com.fleetingtrails.fleetingjobsbackend.auth.entity.PermissionEntity;
import com.fleetingtrails.fleetingjobsbackend.auth.entity.RoleEntity;
import com.fleetingtrails.fleetingjobsbackend.auth.enums.AuthProvider;
import com.fleetingtrails.fleetingjobsbackend.auth.repository.PermissionRepository;
import com.fleetingtrails.fleetingjobsbackend.auth.repository.RoleRepository;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AuthService {

    private static final String SUBSCRIBER_ROLE = "SUBSCRIBER";
    private static final int EMAIL_TOKEN_HOURS = 24;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final EmailService emailService;
    private final OAuthService oAuthService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            PermissionRepository permissionRepository,
            EmailService emailService,
            OAuthService oAuthService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
        this.emailService = emailService;
        this.oAuthService = oAuthService;
    }

    public AuthResponseDto login(LoginRequestDto request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (matches(request.getPassword(), user.getPassword())) {
            assertEmailVerified(user);
            return toAuthenticatedResponse(user);
        }

        if (matches(request.getPassword(), user.getOtp()) || Objects.equals(request.getPassword(), user.getOtp())) {
            return toPasswordSetupRequiredResponse(user);
        }

        throw new BadCredentialsException("Invalid email or password");
    }

    @Transactional
    public SignupResponseDto signup(SignupRequestDto request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        String[] names = OAuthService.splitFullName(request.getFullName());
        String token = UUID.randomUUID().toString();

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setFirstName(names[0]);
        user.setLastName(names[1]);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(requireSubscriberRole());
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setEmailVerified(false);
        user.setEmailVerificationToken(token);
        user.setEmailVerificationExpiresAt(LocalDateTime.now().plusHours(EMAIL_TOKEN_HOURS));
        user.setOtp(null);
        userRepository.save(user);

        emailService.sendEmailConfirmation(email, token);
        return SignupResponseDto.confirmationSent(email);
    }

    @Transactional
    public AuthResponseDto confirmEmail(ConfirmEmailRequestDto request) {
        return confirmEmailToken(request.getToken());
    }

    @Transactional
    public AuthResponseDto confirmEmailToken(String token) {
        UserEntity user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid or expired confirmation token"
                ));

        if (user.getEmailVerificationExpiresAt() == null
                || user.getEmailVerificationExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Confirmation token has expired");
        }

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpiresAt(null);
        userRepository.save(user);

        return toAuthenticatedResponse(user);
    }

    @Transactional
    public AuthResponseDto signupWithGoogle(GoogleSignupRequestDto request) {
        OAuthService.OAuthProfile profile = oAuthService.verifyGoogleIdToken(request.getIdToken());
        return upsertOAuthSubscriber(profile, AuthProvider.GOOGLE);
    }

    @Transactional
    public AuthResponseDto signupWithLinkedIn(LinkedInSignupRequestDto request) {
        OAuthService.OAuthProfile profile = oAuthService.verifyLinkedInAccessToken(request.getAccessToken());
        return upsertOAuthSubscriber(profile, AuthProvider.LINKEDIN);
    }

    @Transactional
    public AuthResponseDto setPassword(SetPasswordRequestDto request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or OTP"));

        if (user.getOtp() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password has already been set for this account"
            );
        }

        if (!Objects.equals(request.getOtp(), user.getOtp())) {
            throw new BadCredentialsException("Invalid email or OTP");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setOtp(null);
        user.setEmailVerified(true);
        userRepository.save(user);

        return toAuthenticatedResponse(user);
    }

    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private AuthResponseDto upsertOAuthSubscriber(
            OAuthService.OAuthProfile profile,
            AuthProvider provider
    ) {
        String email = profile.getEmail().trim().toLowerCase();
        UserEntity existing = userRepository.findByEmail(email).orElse(null);

        if (existing != null) {
            if (existing.getAuthProvider() != provider) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Email is already registered with a different sign-in method"
                );
            }

            existing.setEmailVerified(true);
            existing.setProviderSubject(profile.getSubject());
            existing.setFirstName(profile.getFirstName());
            existing.setLastName(profile.getLastName());
            userRepository.save(existing);
            return toAuthenticatedResponse(existing);
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());
        user.setPassword(null);
        user.setRole(requireSubscriberRole());
        user.setAuthProvider(provider);
        user.setProviderSubject(profile.getSubject());
        user.setEmailVerified(true);
        user.setOtp(null);
        userRepository.save(user);

        return toAuthenticatedResponse(user);
    }

    private RoleEntity requireSubscriberRole() {
        return roleRepository.findByName(SUBSCRIBER_ROLE)
                .orElseThrow(() -> new IllegalStateException(SUBSCRIBER_ROLE + " role not found"));
    }

    private void assertEmailVerified(UserEntity user) {
        if (user.getAuthProvider() == AuthProvider.LOCAL
                && !Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Please confirm your email before logging in"
            );
        }
    }

    private boolean matches(String raw, String encoded) {
        return raw != null && encoded != null && passwordEncoder.matches(raw, encoded);
    }

    private AuthResponseDto toAuthenticatedResponse(UserEntity user) {
        String jwt = jwtService.generateToken(user);
        List<PermissionEntity> permissions = permissionRepository.findByRole(user.getRole());

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(jwt);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setPermissions(permissions);
        response.setRequiresPasswordSetup(false);
        return response;
    }

    private AuthResponseDto toPasswordSetupRequiredResponse(UserEntity user) {
        AuthResponseDto response = new AuthResponseDto();
        response.setToken(null);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setRequiresPasswordSetup(true);
        return response;
    }
}
