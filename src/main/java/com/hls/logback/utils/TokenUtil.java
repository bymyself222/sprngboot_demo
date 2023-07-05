package com.hls.logback.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class TokenUtil {

    /**
     * 默认密钥
     */
    private static final String DEFAULT_SECRET = "KMD21Z4DK4HIFK23";

    private static final String DEFAULT_DATA_KEY = "SELF";

    private static final String DEFAULT_ISSUER = "HUANG";

    private static final Long DEFAULT_EXPIRE_TIME = 7*24*60*60L;


    public String createToken(Map<String, Object> dataMap){
        return createToken(dataMap, DEFAULT_SECRET);
    }

    public String createToken(Map<String, Object> dataMap, String secret){
        // 指定使用的加密算法
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withClaim(DEFAULT_DATA_KEY, dataMap)
                .withIssuer(DEFAULT_ISSUER)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + DEFAULT_EXPIRE_TIME * 1000))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public Map<String, Object> parseToken(String token){
        return parseToken(token, DEFAULT_SECRET);
    }

    public Map<String, Object> parseToken(String token, String secret){
        // 指定使用的加密算法
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim(DEFAULT_DATA_KEY).asMap();
    }

}