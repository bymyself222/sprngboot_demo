package com.hls.logback.controller;

import com.hls.logback.Enum.ExceptionEnum;
import com.hls.logback.Exception.BizException;
import com.hls.logback.annotation.LimitCount;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequestMapping("login")
@RestController
public class LoginController {

    @GetMapping("/userLogin")
    @LimitCount(key = "login", name = "登录接口", prefix = "limit")
    public String login(String username, String password, HttpServletRequest request) throws Exception {
        if (StringUtils.equals("kk", username) && StringUtils.equals("123456", password)) {
            throw new BizException(ExceptionEnum.INTERNAL_SERVER_ERROR);
//            return "登录成功";
        }
        return "账户名或密码错误";
    }
}
