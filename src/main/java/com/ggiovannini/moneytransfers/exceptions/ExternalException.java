package com.ggiovannini.moneytransfers.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class ExternalException extends Exception {

    private ExternalExceptionStatus status;

    public ExternalException(String message, ExternalExceptionStatus status) {
        super(message);
        this.status = status;
    }

    public ExternalException(String message, Throwable cause, ExternalExceptionStatus status) {
        super(message, cause);
        this.status = status;
    }

    public ExternalException(Throwable cause, ExternalExceptionStatus status) {
        super(cause);
        this.status = status;
    }

    public int getStatus() {
        return status.getCode();
    }
}

