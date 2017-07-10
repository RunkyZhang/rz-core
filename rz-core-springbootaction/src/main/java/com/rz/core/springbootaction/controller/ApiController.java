package com.rz.core.springbootaction.controller;

import com.rz.core.practice.dynamic.InstrumentationService;
import com.rz.core.springbootaction.service.BeanApplicationContextAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by renjie.zhang on 7/5/2017.
 */
@RestController
public class ApiController {
    @Value(value = "${app.user.name}")
    private String name;

    @RequestMapping(value = "v3/message/push", method = RequestMethod.POST)
    public String push(@RequestBody String appPushMessage) {
        InstrumentationService instrumentationService = new InstrumentationService();

        String[] beanNames = BeanApplicationContextAware.getApplicationContext().getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println("***bean name: " + beanName);
        }

        return "****" + name
                + "---" + appPushMessage
                + "---" + instrumentationService.getSex(false)
                + "---" + InstrumentationService.getName();
    }
}
