package com.fleetingtrails.fleetingjobsbackend.common.response;

import lombok.Getter;
import lombok.Setter;

public class APIGetResponse<T> {
    @Getter
    @Setter
    private Boolean success;
    @Getter
    @Setter
    private T data;
    @Getter
    @Setter
    private String message = "";

    public static <T> APIGetResponse<T> success (T data) {
        APIGetResponse<T> response = new APIGetResponse<T>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }
    public static <T> APIGetResponse<T> success (T data, String message) {
        APIGetResponse<T> res =  APIGetResponse.success(data);
        res.setMessage(message);
        return res;
    }

}
