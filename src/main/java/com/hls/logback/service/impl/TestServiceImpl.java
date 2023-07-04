package com.hls.logback.service.impl;

import com.hls.logback.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Override
    public String add() {
        log.info("testServie");
        return "testServie -- add";
    }
}
