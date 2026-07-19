package com.fleetingtrails.fleetingjobsbackend.parser.entity;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ParserTemplateType {

    private String company;
    private Integer version;
    private String startUrl;

    private PostingCount postingCount;
    private Listing listing;
    private JobDetails jobDetails;
    private Pagination pagination;

    @Data
    public static class PostingCount {
        private String selector;
    }

    @Data
    public static class Listing {
        private String container;
        private Map<String, Field> fields;
    }

    @Data
    public static class Field {
        private String type;
        private String selector;

        // Only used for attribute fields
        private String attribute;
        private Boolean absolute;
    }

    @Data
    public static class JobDetails {
        private String description;
    }

    @Data
    public static class Pagination {

        /**
         * url_parameter, next_button, infinite_scroll, etc.
         */
        private String type;

        private String parameter;
        private Integer start;
        private Integer increment;
        private Integer pageSize;

        private Map<String, String> additionalParameters;
    }
}