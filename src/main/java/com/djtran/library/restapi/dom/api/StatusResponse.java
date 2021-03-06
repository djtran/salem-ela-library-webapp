package com.djtran.library.restapi.dom.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusResponse {

    private String statusMessage;
    private Code statusCode;

    public enum Code {
        SUCCESS, UNAVAILABLE, ERROR
    }
}
