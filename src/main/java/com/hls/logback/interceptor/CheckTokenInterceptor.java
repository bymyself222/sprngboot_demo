package com.hls.logback.interceptor;

import com.hls.logback.enums.ExceptionEnum;
import com.hls.logback.exceptions.BizException;
import com.hls.logback.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class CheckTokenInterceptor implements HandlerInterceptor {

    @Autowired
    TokenUtil tokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //因为是在请求头中发送token，所以第一次请求的方法为"OPTIONS"，具体可以看TCP/IP协议
        String method = request.getMethod();
        if("OPTIONS".equalsIgnoreCase(method)){
            return true;
        }
        String token = request.getHeader("token");
        System.out.println("token:"+token);
        if(token == null){
            throw new BizException(ExceptionEnum.LOGIN_ERROR);
        }else{
            Map<String, Object> stringObjectMap = tokenUtil.parseToken(token);
            return true;
        }
    }

}
