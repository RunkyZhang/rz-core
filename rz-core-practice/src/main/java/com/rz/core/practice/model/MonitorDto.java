package com.rz.core.practice.model;

import java.io.Closeable;
import java.io.IOException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MonitorDto extends DtoBase implements Closeable {
    private static final long serialVersionUID = 1L;

    private final int fNumber = 222;
    private static final String sfString = "asd";
    public boolean pBool;

    @TagAnnotation(value = "TagAnnotation")
    private String name;
    private int age;
    private MapDto map;

    public MonitorDto() {
        this(true);
    }

    public MonitorDto(boolean flag) {
        this.run(flag);

        this.pBool = true;
        this.name = "11111";
        this.age = 22222;
        this.map = new MapDto();
        this.map.setEnglishName("englishName");
        this.map.setResult(true);
    }

    public String run() {
        return MonitorDto.sfString;
    }

    private String run(boolean flag) {
        return String.valueOf(this.pBool & flag);
    }

    @Override
    public void close() throws IOException {
    }
}