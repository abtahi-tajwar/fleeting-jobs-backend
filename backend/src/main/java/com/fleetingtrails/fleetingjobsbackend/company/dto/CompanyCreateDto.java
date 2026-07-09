package com.fleetingtrails.fleetingjobsbackend.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class CompanyCreateDto {
    @NotBlank(message = "Company name is required")
    @Setter
    @Getter
    private String name;

    @NotBlank(message = "Listing URL is required")
    @Setter
    @Getter
    private String listingUrl;

    @Setter
    @Getter
    private String singlePageUrlTemplate;

    @NotNull(message = "Enabled flag is required")
    @Setter
    @Getter
    private Boolean enabled = true;

    public CompanyCreateDto () { }
}
