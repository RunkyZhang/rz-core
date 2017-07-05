package com.rz.core.springbootaction.controller;

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

        return name + appPushMessage;
    }
}
