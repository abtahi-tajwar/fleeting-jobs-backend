package com.fleetingtrails.fleetingjobsbackend.parser.entity;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ParserTemplateType {

    private String company;
    private Integer version;
    private String listingUrl;

    private PostingCount postingCount;
    private Listing listing;
    private JobDetails jobDetails;
    private Pagination pagination;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PostingCount {
        private String selector;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Listing {
        private String container;
        private Map<String, Field> fields;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Field {
        private String type;
        private String selector;
        private String attribute;
        private Boolean absolute;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class JobDetails {
        private String description;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Pagination {

        private String type;

        private String parameter;
        private Integer start;
        private Integer increment;
        private Integer pageSize;

        private Map<String, String> additionalParameters;
    }
}