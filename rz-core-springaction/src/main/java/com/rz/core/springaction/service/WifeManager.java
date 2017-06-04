package com.rz.core.springaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WifeManager {
    @Autowired
    private HusbandManager husbandManager;
    
    @Override
    public String toString() {
        this.husbandManager.toString();

        return super.toString();
    }
}
