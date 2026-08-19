package com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CertificationCreateDto {
    private String name;
    private String issuer;
    private LocalDate issuedDate;
    private LocalDate expiryDate;
    private String credentialId;
    private String verificationUrl;
}