package com.hls.logback.controller;

import com.hls.logback.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("add")
    public String add(String value){
        String add = testService.add();
        System.out.println(add);
        return value + add;
    }
}
