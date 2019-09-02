package com.x.sdk.mcs.exception;


import com.x.sdk.exception.PaasRuntimeException;

public class CacheClientException extends PaasRuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -2196845577057650318L;

    private static final String MCS_MSG = "MCS RUNTIME ERROR";

    public CacheClientException(Exception ex) {
        super(MCS_MSG, ex);
    }

    public CacheClientException(String message) {
        super(message);
    }
}
