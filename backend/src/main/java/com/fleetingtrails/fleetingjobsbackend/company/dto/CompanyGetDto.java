package com.fleetingtrails.fleetingjobsbackend.company.dto;

import lombok.Getter;
import lombok.Setter;

public class CompanyGetDto {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String listingUrl;
    @Getter
    @Setter
    private String singlePageUrlTemplate;
    @Getter
    @Setter
    private Boolean enabled = true;

    public CompanyGetDto () { }
}
