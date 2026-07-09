package com.fleetingtrails.fleetingjobsbackend.common.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class APIErrorResponse {
    @Getter
    @Setter
    private Boolean success;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private Map<String, String> errors;

    public static APIErrorResponse of (String message) {
        APIErrorResponse response = new APIErrorResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    public static APIErrorResponse of (String message, Map<String, String> errors) {
        APIErrorResponse response = of(message);
        response.setErrors(errors);
        return response;
    }
}
