package com.rz.core.springaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HusbandManager {
    @Autowired
    private WifeManager wifeManager;
    
    @Override
    public String toString() {
        this.wifeManager.toString();

        return super.toString();
    }
}
