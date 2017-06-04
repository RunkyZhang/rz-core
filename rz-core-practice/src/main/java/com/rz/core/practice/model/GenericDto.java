package com.rz.core.practice.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class GenericDto<T0 extends DtoBase, T2, T3 extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Class<T0> genericClass1;
    private Class<T2> genericClass2;
    private Class<T3> genericClass3;

    private T0 _monitor;

    public GenericDto(Class<T0> genericClass1, Class<T2> genericClass2, Class<T3> genericClass3) {
        this.genericClass1 = genericClass1;
        this.genericClass2 = genericClass2;
        this.genericClass3 = genericClass3;
    }
}
