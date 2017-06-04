package com.rz.core.springaction.service;

public class AdpersoninManager {
    private AdvertisementManager advertisementManager;

    public AdpersoninManager() {
    }

    public AdvertisementManager getAdvertisementManager() {
        return this.advertisementManager;
    }

    public void setAdvertisementManager(AdvertisementManager advertisementManager) {
        this.advertisementManager = advertisementManager;
    }
}
