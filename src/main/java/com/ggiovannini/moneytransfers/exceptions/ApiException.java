package com.ggiovannini.moneytransfers.exceptions;

public class ApiException extends Exception {

    private ApiExceptionStatus status;

    public ApiException(String message, ApiExceptionStatus status) {
        super(message);
        this.status = status;
    }

    public ApiException(String message, Throwable cause, ApiExceptionStatus status) {
        super(message, cause);
        this.status = status;
    }

    public ApiException(Throwable cause, ApiExceptionStatus status) {
        super(cause);
        this.status = status;
    }

    public int getStatus() {
        return status.getCode();
    }
}
