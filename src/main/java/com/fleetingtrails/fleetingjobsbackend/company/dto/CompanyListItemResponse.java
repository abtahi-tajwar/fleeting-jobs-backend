package com.fleetingtrails.fleetingjobsbackend.company.dto;

public class CompanyListItemResponse {
    private Long id;

    private String name;

    private String listingUrl;

    private String singlePageUrlTemplate;

    private Boolean enabled;

    public CompanyListItemResponse() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getListingUrl() {
        return listingUrl;
    }

    public void setListingUrl(String listingUrl) {
        this.listingUrl = listingUrl;
    }

    public String getSinglePageUrlTemplate() {
        return singlePageUrlTemplate;
    }

    public void setSinglePageUrlTemplate(String singlePageUrlTemplate) {
        this.singlePageUrlTemplate = singlePageUrlTemplate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
