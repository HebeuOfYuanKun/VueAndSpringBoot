package com.yuankun.security;

import com.yuankun.exception.CaptchaException;
import io.lettuce.core.RedisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {//验证码过滤器
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //System.out.println("url"+httpServletRequest.getRequestURI());
        //System.out.println("method"+httpServletRequest.getMethod());
        if(httpServletRequest.getRequestURI().equalsIgnoreCase("/login")&&httpServletRequest.getMethod().equalsIgnoreCase("POST")){
            try{
                /*System.out.println("验证码");*/
                validate(httpServletRequest);
            }catch (CaptchaException e){
                loginFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
            }catch (RedisException e){
                System.out.println(e);
            } catch (Exception e){
                //loginFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
                System.out.println("其他异常："+e);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validate(HttpServletRequest httpServletRequest) {
        String code = httpServletRequest.getParameter("code");
        String key=httpServletRequest.getParameter("key");
        //System.out.println("code"+code);
        //System.out.println("key"+key);
        if(code==null||key==null||code.equals("")||key.equals("")){
            //System.out.println("验证码错误");
            throw new CaptchaException("验证码错误");
        }

        String realCode = (String) redisTemplate.opsForValue().get(key);
        //System.out.println(realCode);
        System.out.println("真正验证码："+realCode);
        //System.out.println("真正验证码："+realCode.equalsIgnoreCase(code));
        if(!code.equalsIgnoreCase(realCode)){
            throw new CaptchaException("验证码错误");
        }
        redisTemplate.delete(key);
    }
}
