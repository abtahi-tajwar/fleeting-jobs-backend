package com.fleetingtrails.fleetingjobsbackend.common.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class APIListResponse<T> {
    @Setter
    @Getter
    private boolean success;
    @Setter
    @Getter
    private List<T> data;

    public static <T> APIListResponse<T> toSuccessResponse (List<T> data) {
        APIListResponse<T> response = new APIListResponse<T>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }
}
