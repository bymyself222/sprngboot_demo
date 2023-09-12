package com.hls.logback.controller;

import cn.hutool.core.codec.BCD;
import com.fazecast.jSerialComm.SerialPort;
import com.hls.logback.common.MessageListener;
import com.hls.logback.service.TestService;
import com.hls.logback.utils.SerialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    TestService testService;

    @Autowired
    SerialService serialService;

    @GetMapping("add")
    public String add(String value){
        String add = testService.add();
        System.out.println(add);
        return value + add;
    }

    @GetMapping("access")
    public String access(){
        if(testService.tryAcquire()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "access";
        }else{
            return "no access";
        }
    }

    @GetMapping("serial")
    public String serialTest(){
        SerialPort serialPort = serialService.connectSerialPort("COM7");
        Map<String, Boolean> portStatus = serialService.getPortStatus();
        for (String k: portStatus.keySet()) {
            System.out.println(k+"----"+portStatus.get(k));
        }
        serialPort.addDataListener(new MessageListener());
        return "test serial";
    }
}
