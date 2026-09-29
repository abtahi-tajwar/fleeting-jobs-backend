package com.fleetingtrails.fleetingjobsbackend.auth.dto;

import lombok.Data;

@Data
public class SignupResponseDto {
    private String message;
    private String email;
    private boolean requiresEmailConfirmation;

    public static SignupResponseDto confirmationSent(String email) {
        SignupResponseDto response = new SignupResponseDto();
        response.setEmail(email);
        response.setRequiresEmailConfirmation(true);
        response.setMessage("Account created. Please check your email to confirm your address.");
        return response;
    }
}
