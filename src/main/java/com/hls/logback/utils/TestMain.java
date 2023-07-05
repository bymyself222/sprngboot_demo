package com.hls.logback.utils;

import com.alibaba.fastjson.JSONObject;

import java.net.UnknownHostException;

public class TestMain {

    public static void main(String[] args) throws UnknownHostException {
        JSONObject info = SystemInfoUtils.getInfo();
        info.entrySet().forEach(key->{
            JSONObject child = (JSONObject)info.get(key);
            System.out.println(key);
        });
    }
}
