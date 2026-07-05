package com.fleetingtrails.fleetingjobsbackend.company.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "listing_url", nullable = false)
    private String listingUrl;
    @Column(name = "single_page_url_template", nullable = true)
    private String singlePageUrlTemplate;
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

//    public Long getId() {
//        return id;
//    }

//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getListingUrl() {
//        return listingUrl;
//    }
//
//    public void setListingUrl(String listingUrl) {
//        this.listingUrl = listingUrl;
//    }
//
//    public String getSinglePageUrlTemplate() {
//        return singlePageUrlTemplate;
//    }
//
//    public void setSinglePageUrlTemplate(String singlePageUrlTemplate) {
//        this.singlePageUrlTemplate = singlePageUrlTemplate;
//    }
//
//    public Boolean getEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(Boolean enabled) {
//        this.enabled = enabled;
//    }

}
