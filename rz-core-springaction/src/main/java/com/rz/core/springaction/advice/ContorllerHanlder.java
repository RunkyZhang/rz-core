package com.rz.core.springaction.advice;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by renjie.zhang on 7/27/2017.
 */
@ControllerAdvice
public class ContorllerHanlder {
//    @ModelAttribute
//    public MonitorDto newMonitor() {
//        System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前把返回值放入Model");
//        return new MonitorDto();
//    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器");
    }

    @ExceptionHandler(Exception.class)
    public Object defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        System.out.println("============all exception: " + e.getMessage());
        return "============all exception: " + e.getMessage();
    }

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public String processUnauthenticatedException(NativeWebRequest request, UnauthenticatedException e) {
//        System.out.println("===========应用到所有@RequestMapping注解的方法，在其抛出UnauthenticatedException异常时执行");
//        return "viewName"; //返回一个逻辑视图名
//    }
}

