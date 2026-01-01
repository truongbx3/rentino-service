package com.viettel.vss.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;

/**
 * The Class BusinessException.
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(BusinessException.class);

    private String code;
    private Serializable errorData;

    public BusinessException() {
        this(null);
    }

    public BusinessException(String code) {
        this(code, code);
    }

    public BusinessException(String code, String message) {
        this(code, message, null);
    }

    public BusinessException(String code, String message, Throwable cause) {
        this(code, message, null, cause);
    }

    public BusinessException(String code, String message, Serializable errorData, Throwable cause) {
        super(message, cause);
        this.code = code;

        if (errorData != null) {
            this.errorData = errorData;
        } else if (message != null) {
            if (logger.isDebugEnabled()) {
                this.errorData = new HashMap<>(Collections.singletonMap("messsage", message));
            } else {
                this.errorData = new HashMap<>();
            }
        }
        logger.error(String.format("BusinessException(code=[%s], message=[%s])", code, message), cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Serializable getErrorData() {
        return errorData;
    }

    public void setErrorData(Serializable errorData) {
        this.errorData = errorData;
    }

}
