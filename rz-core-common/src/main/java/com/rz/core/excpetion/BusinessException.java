package com.rz.core.excpetion;

/**
 * Created by renjie.zhang on 10/30/2017.
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
