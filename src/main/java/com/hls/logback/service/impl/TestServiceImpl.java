package com.hls.logback.service.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.hls.logback.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

    //每秒5个令牌
    RateLimiter limiter = RateLimiter.create(5.0);

    @Override
    public String add() {
        log.info("testServie");
        return "testServie -- add";
    }

    @Override
    public Boolean tryAcquire() {
        return limiter.tryAcquire();
    }
}
