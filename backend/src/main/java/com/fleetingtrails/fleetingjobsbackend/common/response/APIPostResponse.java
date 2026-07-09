package com.fleetingtrails.fleetingjobsbackend.common.response;

import lombok.Getter;
import lombok.Setter;

public class APIPostResponse<T> {
    @Getter
    @Setter
    private Boolean success;
    @Getter
    @Setter
    private T data;

    public static <T> APIPostResponse<T> success (T data) {
        APIPostResponse<T> response = new APIPostResponse<T>();
        response.setSuccess(true);
        response.setData(data);

        return response;
    }
}
