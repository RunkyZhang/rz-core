package com.rz.core.practice.model;

import java.io.Serializable;

//import lombok.Data;

//A a = new A();
//B b = new B();
//a.setB(b);
//b.setA(a);
//a.hashCode();
// A <-> B has loop reference issue, if use @Data

//@Data
public class A implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private B b;

    public void setB(B b) {
        this.b = b;
    }
}
