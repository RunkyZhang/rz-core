package com.rz.core.springaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameMachineManager {
    @Autowired
    private GameManager gameManager;

    @Override
    public String toString() {
        this.gameManager.toString();

        return super.toString();
    }
}
