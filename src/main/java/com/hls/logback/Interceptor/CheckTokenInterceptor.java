package com.hls.logback.Interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hls.logback.common.ResultBody;
import com.hls.logback.utils.TokenUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
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
            doResponse(response,ResultBody.error("登录失败"));
        }else{
            Map<String, Object> stringObjectMap = tokenUtil.parseToken(token);
            return true;
        }
        return false;
    }

    @SneakyThrows
    private void doResponse(HttpServletResponse response, ResultBody responseBody) {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String s = new ObjectMapper().writeValueAsString(responseBody);
        out.print(s);
        out.flush();
        out.close();
    }
}
