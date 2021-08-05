package com.yuankun.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
@Data
@Component
@ConfigurationProperties(prefix = "yuankun.jwt")
public class JWTUtils {
    /*
    封装jwt
     */
    private String sign;//密匙
    private Integer expired;
    public String getToken(Map<String,Object> map){
        JWTCreator.Builder builder=JWT.create();
        map.forEach((k,v)->{
            builder.withClaim(k,String.valueOf(v));
        });
        String Token=builder.withExpiresAt(new Date(new Date().getTime()+1000*expired))
                .sign(Algorithm.HMAC256(sign));
        return Token;
    }

    public DecodedJWT verifyToken(String Token){
        DecodedJWT decodedJWT=null;
        try{
            decodedJWT= JWT.require(Algorithm.HMAC256(sign)).build().verify(Token);
        }catch (Exception e){
            return null;
        }
        return decodedJWT;
    }
    //验证是否token过期
    public static boolean IsTokenExpired(DecodedJWT decodedJWT){
        System.out.println(new Date());
        if(decodedJWT.getExpiresAt().before(new Date())){
            System.out.println("时间"+true);
            return true;
        }
        return false;
    }
}
