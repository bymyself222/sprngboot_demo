package com.hls.logback.aspect;

import com.google.common.collect.ImmutableList;
import com.hls.logback.annotation.LimitCount;
import com.hls.logback.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class LimitCountAspect {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    //作用在注解上
    @Pointcut("@annotation(com.hls.logback.annotation.LimitCount)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();

        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        LimitCount annotation = method.getAnnotation(LimitCount.class);
        String name = annotation.name();
        String key = annotation.key();
        String prefix = annotation.prefix();
        int period = annotation.period();
        int count = annotation.count();
        //访问IP
        String ip = IPUtil.getIpAddr(request);
        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(prefix + "_", key, ip));
        String luaScript = buildLuaScript();
        RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
        Number number = redisTemplate.execute(redisScript, keys, count, period);
        log.info("IP:{} 第 {} 次访问key为 {}，描述为 [{}] 的接口", ip, number, keys, name);
        if (number != null && number.intValue() <= count) {
            return point.proceed();
        } else {
            return "接口访问超出频率限制";
        }

    }

    /**
     * 限流脚本
     * 调用的时候不超过阈值，则直接返回并执行计算器自加。
     *
     * @return lua脚本
     */
    private String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }
}
