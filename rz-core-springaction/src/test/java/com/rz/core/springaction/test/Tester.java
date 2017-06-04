package com.rz.core.springaction.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rz.core.Assert;
import com.rz.core.springaction.config.ApplcationConfig;
import com.rz.core.springaction.service.AdpersoninManager;
import com.rz.core.springaction.service.GameShopManager;
import com.rz.core.springaction.service.GamerManagaer;
import com.rz.core.springaction.service.WifeManager;

@ActiveProfiles(profiles = { "test" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplcationConfig.class })
public class Tester {
    @Autowired
    private AdpersoninManager adpersoninManager;

    @Autowired
    private GameShopManager gameShopManager;

    @Autowired
    private WifeManager wifeManager;

    @Autowired
    private GamerManagaer gamerManagaer;

    @Test
    public void notNullAdpersoninManager() {
        Assert.isNotNull(this.adpersoninManager, "adpersoninManager");
    }

    @Test
    public void notNullGameShopManager() {
        Assert.isNotNull(this.gameShopManager, "gameShopManager");
    }

    @Test
    public void notNullWifeManager() {
        Assert.isNotNull(this.wifeManager, "wifeManager");
    }

    @Test
    public void notNullGamerManagaer() {
        Assert.isNotNull(this.gamerManagaer, "gamerManagaer");
    }
}
