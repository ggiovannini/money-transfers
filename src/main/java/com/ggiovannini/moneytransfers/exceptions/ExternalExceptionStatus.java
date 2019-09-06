package com.ggiovannini.moneytransfers.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public enum ExternalExceptionStatus {
    BAD_REQUEST(HttpStatus.BAD_REQUEST_400),
    NOT_FOUND(HttpStatus.NOT_FOUND_404);

    int code;

    ExternalExceptionStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
