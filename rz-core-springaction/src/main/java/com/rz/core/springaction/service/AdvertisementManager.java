package com.rz.core.springaction.service;

import java.util.List;

public class AdvertisementManager {
    private String name;
    private int duration;
    private List<String> words;
    private Object image;

    public AdvertisementManager(String name, int duration, List<String> words, Object image) {
        this.name = name;
        this.duration = duration;
        this.words = words;
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<String> getWords() {
        return this.words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
    
    public Object getImage() {
        return this.image;
    }

    public void setImage(Object image) {
        this.image = image;
    }
}
