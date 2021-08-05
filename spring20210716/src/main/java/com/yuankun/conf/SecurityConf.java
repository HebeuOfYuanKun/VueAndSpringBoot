
package com.yuankun.conf;


import com.yuankun.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration//配置springsecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConf extends WebSecurityConfigurerAdapter {
    private static final String[] AUTH_WHILTE={"/login","/captcha","/test/pass"};
    @Autowired
    private UserDetailsService userDetailsService;//管理员信息
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private CaptchaFilter captchaFilter;
    /*@Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;*/
    @Autowired
    private JWTAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JWTLogoutHandler jwtLogoutHandler;
    /*@Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;*/
    @Bean//注入自己的过滤器
    public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception{
        JWTAuthenticationFilter jwtAuthenticationFilter=new JWTAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean//注册一个BCrypt加密
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .formLogin()
                //.loginProcessingUrl("/login")
                    .failureHandler(loginFailureHandler)
                    .successHandler(loginSuccessHandler)
                .and()
                    .logout()
                    .logoutSuccessHandler(jwtLogoutHandler)

                .and()//禁用session
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()//设置权限问题
                    .authorizeRequests()
                    .antMatchers(AUTH_WHILTE).permitAll()
                    .anyRequest().authenticated()

                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()//设置验证码先进行校验
                    .addFilter(jwtAuthenticationFilter())
                    .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);



                /*.loginProcessingUrl("/admin/login1")
                .defaultSuccessUrl("/admin/loginsucceed").permitAll()
                .and().authorizeRequests()
                .antMatchers("/admin/login","/","/login.html","/admin/login1").permitAll()
                //.antMatchers("/admin/loginsucceed").hasAuthority("admin")
                .anyRequest().authenticated()*/

    }
}

