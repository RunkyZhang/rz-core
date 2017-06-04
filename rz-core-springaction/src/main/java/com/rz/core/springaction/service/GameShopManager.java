package com.rz.core.springaction.service;

public class GameShopManager {
    private SalesManManager salesManManager;

    public GameShopManager(SalesManManager salesManManager) {
        this.salesManManager = salesManManager;
    }

    @Override
    public String toString() {
        this.salesManManager.toString();

        return super.toString();
    }
}
