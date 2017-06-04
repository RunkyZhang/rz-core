package com.rz.core.practice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class TypeDto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String tString;
    private int tint;
    private Integer tInteger;
    private boolean tbool;
    private Boolean tBoolean;
    private char tchar;
    private Character tCharacter;
    private byte tb;
    private Byte tByte;
    private short ts;
    private Short tShort;
    private long tl;
    private Long tLong;
    private float tf;
    private Float tFloat;
    private double td;
    private Double tDouble;
    private Void tVoid;
    private int[] tints;
    private MonitorDto monitorDto;
    // private TypeDto myself; // it is work, but cannot run .hashCode and
    // toString
    private TypeDto next;
    private List<String> tStrings;
    private Map<Integer, MonitorDto> monitorDtos;

    public static TypeDto build() {
        TypeDto typeDto = new TypeDto();
        typeDto.tString = "0";
        typeDto.tint = 1;
        typeDto.tInteger = 2;
        typeDto.tbool = true;
        typeDto.tBoolean = false;
        typeDto.tchar = 'a';
        typeDto.tCharacter = 'b';
        typeDto.tb = 3;
        typeDto.tByte = 4;
        typeDto.ts = 5;
        typeDto.tShort = 6;
        typeDto.tl = 7;
        typeDto.tLong = 8l;
        typeDto.tf = 9.1f;
        typeDto.tFloat = 10.1f;
        typeDto.td = 11.1d;
        typeDto.tDouble = 12.1d;
        typeDto.tVoid = null;
        typeDto.tints = new int[] { 13, 14, 15, 16, 17 };
        MapDto mapDto = new MapDto();
        mapDto.setResult(true);
        mapDto.setEnglishName("18");
        typeDto.monitorDto = new MonitorDto();
        typeDto.monitorDto.setAge(19);
        typeDto.monitorDto.setName("20");
        typeDto.monitorDto.setMap(mapDto);
        // typeDto.myself = typeDto;
        typeDto.next = new TypeDto();
        typeDto.next.tLong = 1000l;
        typeDto.next.tString = "1000";
        typeDto.tStrings = new ArrayList<>();
        typeDto.monitorDtos = new HashMap<>(); 
        for (int i = 0; i < 10; i++) {
            typeDto.tStrings.add(i + "s");
            
            MonitorDto monitorDto = new MonitorDto();
            monitorDto.setAge(i * -1);
            monitorDto.setName(i + "monitorDto");
            mapDto = new MapDto();
            mapDto.setResult(false);
            mapDto.setEnglishName("18 mapDto");
            monitorDto.setMap(mapDto);
            typeDto.monitorDtos.put(i, monitorDto);
        }

        return typeDto;
    }

    public static void change(TypeDto typeDto) {
        typeDto.tString = typeDto.tString + 1;
        typeDto.tint++;
        typeDto.tInteger++;
        typeDto.tbool = !typeDto.tbool;
        typeDto.tBoolean = !typeDto.tBoolean;
        typeDto.tchar = 'x';
        typeDto.tCharacter = 'y';
        typeDto.tb++;
        typeDto.tByte++;
        typeDto.ts++;
        typeDto.tShort++;
        typeDto.tl++;
        typeDto.tLong++;
        typeDto.tf++;
        typeDto.tFloat++;
        typeDto.td++;
        typeDto.tDouble++;
        typeDto.tVoid = null;
        typeDto.tints[0]++;
        typeDto.tints[1]++;
        typeDto.tints[2]++;
        typeDto.tints[3]++;
        typeDto.tints[4]++;
        typeDto.monitorDto.setAge(typeDto.monitorDto.getAge() + 1);
        typeDto.monitorDto.setName(typeDto.monitorDto.getName() + 1);
        typeDto.monitorDto.getMap().setResult(false);
        typeDto.monitorDto.getMap().setEnglishName("19");
        typeDto.next.tLong = 1000l + 1;
        typeDto.next.tString = "1000" + 1;
        for (int i = 0; i < 10; i++) {
            typeDto.tStrings.set(i, i + "s" + 1);
            
            MonitorDto monitorDto = new MonitorDto();
            monitorDto.setAge(i * -1 + 1);
            monitorDto.setName(i + "monitorDto" + 1);
            MapDto mapDto = new MapDto();
            mapDto.setResult(true);
            mapDto.setEnglishName("18 mapDto" + 1);
            monitorDto.setMap(mapDto);
            typeDto.monitorDtos.put(i, monitorDto);
        }
    }
}
