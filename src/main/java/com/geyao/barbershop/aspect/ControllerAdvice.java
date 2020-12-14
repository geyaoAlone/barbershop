package com.geyao.barbershop.aspect;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

//@RestControllerAdvice(basePackages = "com.geyao.barbershop")
//public class ControllerAdvice {
//
//    @ExceptionHandler({RuntimeException.class})
//    JSONObject handleException(HttpServletRequest request, Throwable ex) {
//        JSONObject errorJ = new JSONObject();
//        errorJ.put("code","0");
//        errorJ.put("msg","系统异常");
//        return errorJ;
//    }
//}
