package com.rz.core.dao.excpetion;

/**
 * Created by Runky on 2/3/2018.
 */
public class DaoException extends RuntimeException {
    public DaoException(Throwable e) {
        super(e);
    }

    public DaoException(String message) {
        super(message);
    }
}