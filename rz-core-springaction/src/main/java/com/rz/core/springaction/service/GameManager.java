package com.rz.core.springaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("gameManager_beanName")
public class GameManager {
    // bean name or qualifier value
    @Qualifier("houhou")
    //@Qualifier("testDevManagerImpl")
    @Autowired
    private DevManager devManager;
}
