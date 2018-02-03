package com.rz.core.excpetion;

import com.rz.core.Assert;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by renjie.zhang on 10/30/2017.
 */
public class ExceptionDefinition {
    private String key;
    private int code;
    private String message;
    private String defaultMessage;

    public String getKey() {
        return this.key;
    }

    public int getCode() {
        return this.code;
    }

    public String getDefaultMessage() {
        return this.defaultMessage;
    }

    public String getMessage() {
        return this.message;
    }

    public ExceptionDefinition(String key, int code, String defaultMessage) {
        Assert.isNotBlank(key, "key");
        Assert.isNotBlank(defaultMessage, "defaultMessage");

        this.key = key;
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.message = this.defaultMessage;
    }

    public BusinessException toException() {
        return this.toException(null, false, null);
    }

    public BusinessException toException(String message) {
        return this.toException(message, true, null);
    }

    public BusinessException toException(Throwable throwable) {
        return this.toException(null, false, throwable);
    }

    public BusinessException toException(String message, boolean isAppending, Throwable throwable) {
        this.message = null == message ? "" : message;
        this.message = !isAppending || StringUtils.isBlank(this.message) ? this.defaultMessage : this.defaultMessage + "+++" + this.message;
        this.message = null == throwable || StringUtils.isBlank(throwable.getMessage()) ? this.message : this.message + "+++" + throwable.getMessage();
        return new BusinessException(this.getCode(), this.message);
    }

    @Override
    public String toString() {
        return "Code: [" + String.valueOf(this.code) + "] Message: [" + this.message + "]";
    }
}