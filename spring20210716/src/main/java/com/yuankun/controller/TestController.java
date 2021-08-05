package com.yuankun.controller;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yuankun.response.Result;
import com.yuankun.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    JWTUtils jwtUtils;
    @GetMapping("/test")
    public Map<String,Object> token(String username,String password){
        Map<String,Object> map=new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        String token = jwtUtils.getToken(map);
        Map<String,Object> resmap=new HashMap<>();
        resmap.put("state",200);
        resmap.put("msg","登录成功");
        resmap.put("token",token);
        return resmap;
    }
    @GetMapping("/test/pass")
    public Result pass(){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("111111");
        boolean matches = bCryptPasswordEncoder.matches("111111", encode);
        Result result=new Result();
        System.out.println("matches"+matches+"加密后的："+encode);
        result.setObject(encode);

        return result;
    }
    @GetMapping("/verify")
    public Map<String,Object> verify(String token){
        Map<String,Object> map=new HashMap<>();
        try {
            DecodedJWT decodedJWT=jwtUtils.verifyToken(token);
            System.out.println(decodedJWT.getClaim("username").asString());
            System.out.println(decodedJWT.getClaim("password").asString());
        } catch (TokenExpiredException exception) {
            exception.printStackTrace();
            map.put("msg","token过期");
        }catch (SignatureGenerationException exception) {
            exception.printStackTrace();
            map.put("msg","签名错误");
        }catch (AlgorithmMismatchException exception) {
            exception.printStackTrace();
            map.put("msg","加密算法错误");
        }catch (Exception exception) {
            exception.printStackTrace();
            map.put("msg","其他异常");
        }
        return map;
    }
}
