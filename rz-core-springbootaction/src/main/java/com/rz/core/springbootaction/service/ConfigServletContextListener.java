package com.rz.core.springbootaction.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by renjie.zhang on 7/5/2017.
 */
@WebListener
public class ConfigServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("start ServletContextListener" + ServletContextListener.class.getClassLoader().getResource("").getPath());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("end ServletContextListener");
    }
}
