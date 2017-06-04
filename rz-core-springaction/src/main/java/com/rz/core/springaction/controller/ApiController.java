package com.rz.core.springaction.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @RequestMapping(value = "v3/message/push", method = RequestMethod.POST)
    public String push(@RequestBody String appPushMessage) {

        return "";
    }
}
