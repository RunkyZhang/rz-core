package com.rz.core.springaction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {
    @RequestMapping(value = "v3/message/push", method = RequestMethod.POST)
    public ModelAndView push(@RequestBody String appPushMessage) {
        ModelAndView modelAndView = new ModelAndView();

        return modelAndView;
    }
}
