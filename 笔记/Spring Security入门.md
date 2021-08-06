### Spring Security入门

#### 说明

springboot版本2.5.2

#### maven导入依赖包

```xml
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

如果不进行任何配置，无论进入哪个页面，自动进入/login页面，进行认证。默认用户名：user，默认密码：控制台中的Using generated security password: 。

#### 本质就是一堆过滤器链

#### web权限方案

- 认证
- 授权

#### 认证步骤

##### 设置登录的用户名密码

- 配置文件
- 配置类 密码必须采取加密措施，否则会抛出异常
- 自定义编写实现类（推荐）

###### 自定义编写实现类

```java
@Configuration
public class SecurityConfDiy extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
```

```java
@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private usermapper userMapper;
    @Override
    //用户细节定义
    public UserDetails loadUserByUsername(String AdminName) throws UsernameNotFoundException {
        user temuser=userMapper.queryUser(AdminName);
        if(temuser==null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<GrantedAuthority> list= AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
        return new User(temuser.getAdminName(),new BCryptPasswordEncoder().encode(temuser.getAdminPassword()),list);
    }
}
```

#### 授权步骤

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.formLogin().loginPage("/login.html")//登录界面
            .loginProcessingUrl("/admin/login1")//登录提交的请求URL地址
            .defaultSuccessUrl("/admin/loginsucceed").permitAll()//默认登录界面请求
            .and().authorizeRequests()
            .antMatchers("/admin/login","/","/login.html","/admin/login1").permitAll()
            .antMatchers("/admin/loginsucceed").hasAuthority("admin")//有admin权限才行
            .anyRequest().authenticated()
            .and().csrf().disable();
}
```