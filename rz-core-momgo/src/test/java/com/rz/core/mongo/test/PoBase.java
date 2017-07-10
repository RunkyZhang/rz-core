package com.rz.core.mongo.test;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by renjie.zhang on 7/10/2017.
 */
public class PoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    private Object _id;
    private String operationUser;
    private Date createdTime;
    private Date updatedTime;
    private String comment;

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Object getId() {
        return _id;
    }

    public void setId(Object id) {
        this._id = id;
    }
}
