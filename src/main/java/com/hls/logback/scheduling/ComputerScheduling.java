package com.hls.logback.scheduling;

import com.alibaba.fastjson.JSONObject;
import com.hls.logback.enums.ExceptionEnum;
import com.hls.logback.exceptions.BizException;
import com.hls.logback.utils.SystemInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

@Component
public class ComputerScheduling {

//    @Scheduled(cron = "0 */1 * * * ?")
    public void sendSystemInfo() {
        JSONObject info = null;
        try {
            info = SystemInfoUtils.getInfo();
        } catch (UnknownHostException e) {
            throw new BizException(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }
        info.entrySet().forEach(key->{
//            System.out.println(key);
        });
    }
}
