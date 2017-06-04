package com.rz.core.practice.model;

import java.io.Serializable;

//import lombok.Data;

//@Data
public class B implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private A a;

    public void setA(A a) {
        this.a = a;
    }
}
