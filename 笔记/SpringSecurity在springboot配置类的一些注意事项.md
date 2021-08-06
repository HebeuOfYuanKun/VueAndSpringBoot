### SpringSecurity在springboot配置类的一些注意事项

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception { auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("yuankun").password(new BCryptPasswordEncoder().encode("123456")).roles("SuperAdmin");
}
```

- 必须给AuthenticationManagerBuilder表明密码采取什么加密方式，官方推荐BCryptPasswordEncoder。如果指明加密方式会抛出异常，指明方法.passwordEncoder(new BCryptPasswordEncoder())
- 如果不采用上述方式，在配置类中声明@bean也是可以的

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    String encode = passwordEncoder.encode("123456");
    System.out.println(encode);
    auth.inMemoryAuthentication().withUser("yuankun").password(encode).roles("SuperAdmin");
}
@Bean
PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
}
```

无论上述使用哪种方法必须指明roles();