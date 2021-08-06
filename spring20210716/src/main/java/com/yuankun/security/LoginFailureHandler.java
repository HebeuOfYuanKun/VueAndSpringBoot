package com.yuankun.security;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.yuankun.response.Result;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {//登录失败处理器

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream servletOutputStream=httpServletResponse.getOutputStream();
        httpServletResponse.setStatus(402);
        Result result=new Result(402,e.getMessage(),null);


        servletOutputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        servletOutputStream.flush();
        servletOutputStream.close();
    }
}
