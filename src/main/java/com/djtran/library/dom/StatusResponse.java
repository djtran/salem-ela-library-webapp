package com.djtran.library.dom;

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
