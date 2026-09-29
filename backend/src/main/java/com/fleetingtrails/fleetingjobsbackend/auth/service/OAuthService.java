package com.fleetingtrails.fleetingjobsbackend.auth.service;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class OAuthService {
    private final WebClient webClient = WebClient.builder().build();

    @Value("${oauth.google.client-id:}")
    private String googleClientId;

    public OAuthProfile verifyGoogleIdToken(String idToken) {
        Map<?, ?> payload;
        try {
            payload = webClient.get()
                    .uri("https://oauth2.googleapis.com/tokeninfo?id_token={token}", idToken)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google ID token");
        }

        if (payload == null || payload.get("email") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google ID token");
        }

        if (googleClientId != null && !googleClientId.isBlank()) {
            Object audience = payload.get("aud");
            if (audience == null || !googleClientId.equals(audience.toString())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google token audience mismatch");
            }
        }

        String emailVerified = stringValue(payload.get("email_verified"));
        if ("false".equalsIgnoreCase(emailVerified)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Google email is not verified");
        }

        return OAuthProfile.builder()
                .subject(stringValue(payload.get("sub")))
                .email(stringValue(payload.get("email")))
                .firstName(firstNonBlank(stringValue(payload.get("given_name")), "Google"))
                .lastName(firstNonBlank(stringValue(payload.get("family_name")), "User"))
                .build();
    }

    public OAuthProfile verifyLinkedInAccessToken(String accessToken) {
        Map<?, ?> payload;
        try {
            payload = webClient.get()
                    .uri("https://api.linkedin.com/v2/userinfo")
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid LinkedIn access token");
        }

        if (payload == null || payload.get("email") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "LinkedIn profile is missing email");
        }

        Object verified = payload.get("email_verified");
        if (verified instanceof Boolean bool && !bool) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LinkedIn email is not verified");
        }

        String fullName = stringValue(payload.get("name"));
        String given = stringValue(payload.get("given_name"));
        String family = stringValue(payload.get("family_name"));

        if ((given == null || given.isBlank()) && fullName != null && !fullName.isBlank()) {
            String[] parts = splitFullName(fullName);
            given = parts[0];
            family = parts[1];
        }

        return OAuthProfile.builder()
                .subject(stringValue(payload.get("sub")))
                .email(stringValue(payload.get("email")))
                .firstName(firstNonBlank(given, "LinkedIn"))
                .lastName(firstNonBlank(family, "User"))
                .build();
    }

    public static String[] splitFullName(String fullName) {
        String trimmed = fullName == null ? "" : fullName.trim().replaceAll("\\s+", " ");
        if (trimmed.isBlank()) {
            return new String[]{"User", "Subscriber"};
        }

        int space = trimmed.indexOf(' ');
        if (space < 0) {
            return new String[]{trimmed, "-"};
        }

        return new String[]{
                trimmed.substring(0, space),
                trimmed.substring(space + 1).trim()
        };
    }

    private String stringValue(Object value) {
        return value == null ? null : value.toString();
    }

    private String firstNonBlank(String preferred, String fallback) {
        return preferred == null || preferred.isBlank() ? fallback : preferred;
    }

    @Getter
    @Builder
    public static class OAuthProfile {
        private final String subject;
        private final String email;
        private final String firstName;
        private final String lastName;
    }
}
