package com.rz.core.practice.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class NormalDto implements Serializable  {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String age;
}
