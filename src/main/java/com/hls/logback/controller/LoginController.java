package com.hls.logback.controller;

import com.hls.logback.Enum.ExceptionEnum;
import com.hls.logback.Exception.BizException;
import com.hls.logback.annotation.LimitCount;
import com.hls.logback.common.ResultBody;
import com.hls.logback.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("login")
@RestController
public class LoginController {

    @Autowired
    TokenUtil tokenUtil;

    @GetMapping("/userLogin")
    @LimitCount(key = "login", name = "登录接口", prefix = "limit")
    @ResponseBody
    public ResultBody login(String username, String password, HttpServletRequest request) throws Exception {
        if (StringUtils.equals("kk", username) && StringUtils.equals("123456", password)) {
            log.info("登录失败！");
            return ResultBody.error("登陆失败");
        }
        String userId = "001";
        String userRole = "admin";
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("userId", userId);
        dataMap.put("userRole", userRole);

        // 将用户识别信息存储到token中
        String token = tokenUtil.createToken(dataMap);
        log.info("生成的token为：{}", token);
        return ResultBody.success(token);
    }
}
