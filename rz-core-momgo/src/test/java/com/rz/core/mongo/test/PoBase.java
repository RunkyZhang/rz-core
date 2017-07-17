package com.rz.core.mongo.test;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by renjie.zhang on 7/10/2017.
 */
public class PoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    public static String staticValue1 = "staticValue1";


    private Boolean deleted;
    private String operationUser;
    private Date createdTime;
    private Date updatedTime;
    private String comment;

//    private String privateValue1 = "privateValue1";
//    String privateValue2 = "privateValue2";
//    protected String privateValue3 = "privateValue3";
//    private String privateValue4 = "privateValue4";

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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

//    private String getPrivateValue2() {
//        return privateValue2;
//    }
//
//    private void setPrivateValue2(String privateValue2) {
//        this.privateValue2 = privateValue2;
//    }
//
//    public String getPrivateValue3() {
//        return privateValue3;
//    }
//
//    public void setPrivateValue4(String privateValue4) {
//        this.privateValue4 = privateValue4;
//    }
}