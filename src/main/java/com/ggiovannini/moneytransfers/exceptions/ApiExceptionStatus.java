package com.ggiovannini.moneytransfers.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public enum  ApiExceptionStatus {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR_500);

    int code;

    ApiExceptionStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
