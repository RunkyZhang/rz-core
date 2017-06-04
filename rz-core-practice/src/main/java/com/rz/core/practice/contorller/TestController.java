package com.rz.core.practice.contorller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rz.core.practice.service.ITestService;

@RestController
@Scope("prototype")
public class TestController {

    @Autowired
    private ITestService testService;

    @Resource(name = "properties")
    Map<String, String> properties;

    @RequestMapping(value = "/hello/{someone}") // http://localhost:8080/hello/abc
    public String hello(@PathVariable("someone") String someone) throws Exception {
        System.out.println(someone);
        if (true == "asd".equals(someone)) {
            throw new Exception("66666666666666666666666");
        } else {
            return testService.Test("value") + properties.toString();
        }
    }

    @RequestMapping(value = "self_check")
    public List<String> selfCheck(@RequestHeader(value = "monitor-hj") String monitorHJ) {
        if (null == monitorHJ) {

        }

        List<String> results = new ArrayList<>();
        return results;
    }
    
    @RequestMapping(value = "/hello/some", method = RequestMethod.POST)
    public void push(@RequestBody Map<String, Date> appPushMessage) {
        System.out.println(appPushMessage);
    }
}
