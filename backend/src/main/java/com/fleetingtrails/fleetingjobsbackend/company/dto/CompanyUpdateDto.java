package com.fleetingtrails.fleetingjobsbackend.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class CompanyUpdateDto {
    @NotBlank(message = "Company name is required")
    @Getter
    @Setter
    private String name;

    @NotBlank(message = "Listing URL is required")
    @Getter
    @Setter
    private String listingUrl;

    @Getter
    @Setter
    private String singlePageUrlTemplate;

    @NotNull(message = "Enabled flag is required")
    @Getter
    @Setter
    private Boolean enabled = true;

    public CompanyUpdateDto () { }
}
