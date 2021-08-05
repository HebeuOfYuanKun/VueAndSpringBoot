package com.yuankun;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yuankun.mapper.MenuMapper;
import com.yuankun.mapper.RoleMapper;
import com.yuankun.mapper.UserMapper;
import com.yuankun.pojo.User;
import com.yuankun.pojo.dto.UserTable;
import com.yuankun.response.Result;
import com.yuankun.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
class Spring20210716ApplicationTests {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuService menuService;
    @Test
    void contextLoads() {
        //System.out.println(menuMapper.QueryAllMenu());
        //System.out.println(userMapper.QueryByUserName("admin"));
        //System.out.println(roleMapper.QueryRoleByName("用户"));
        //System.out.println(menuService.QueryAlLMenu());
        /*Calendar instance=new GregorianCalendar();
        instance.add(Calendar.SECOND,1000);
        String token= JWT.create()
                .withClaim("username","yuankun")
                .withClaim("userid","190310110")
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256("yuankun"));
        System.out.println(token);*/
        //System.out.println(new Date());
        /*BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("111111");
        String encode1 = bCryptPasswordEncoder.encode("1234567");
        System.out.println("1234567加密后"+encode1);
        boolean matches = bCryptPasswordEncoder.matches("1234567", "$2a$10$0ilP4ZD1kLugYwLCs4pmb.ZT9cFqzOZTNaMiHxrBnVIQUGUwEvBIO");
        Result result=new Result();
        System.out.println("matches的"+matches+"加密后的："+encode);
        result.setObject(encode);*/
    }
    @Test
    void TestJwt(){
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256("yuankun")).build();
        DecodedJWT decodedJWT= jwtVerifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Mjc1NDg4MTEsInVzZXJpZCI6IjE5MDMxMDExMCIsInVzZXJuYW1lIjoieXVhbmt1biJ9.CK5daMNaQgdTQooeq4Bid1UbsBUF5m1Z49wUD-1pCCI");
        System.out.println(decodedJWT.getClaim("username").asString());
        System.out.println(decodedJWT.getClaim("userid").asString());
    }
    @Test
    void TestListToString(){
        List<String> list=new ArrayList<>();
        list.add("小米1");
        list.add("小米2");
        list.add("小米3");
        System.out.println("---------"+list);
        String aa=String.join(",",list);
        System.out.println("--------"+aa);
    }
    @Test
    void TestFather(){
        User user=new UserTable();
        user.setId(1);
        user.setUserName("小米");
        UserTable userTable= (UserTable) user;
        System.out.println(userTable);
        userTable.setUserRole(Arrays.asList(new String[]{"aa", "aaa"}));
        System.out.println(userTable);
    }
    @Test
    void timeTest(){
        System.out.println(DateUtil.date());
    }

}
