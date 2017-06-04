package com.rz.core.practice.model;

public class XChild extends XParent {

    private int age;

    public XChild() {
        this("xxx");
    }

    public XChild(String name) {
        this(name, 0);
    }

    public XChild(String name, int age) {
        super(name);
        
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }
}
