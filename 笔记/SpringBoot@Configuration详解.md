### SpringBoot@Configuration详解

在类上写@Configuration表明这是一个配置类==配置文件，在方法上写@Bean就代表这个方法是一个组件，@Bean("user01")代表注册了一个对象名叫user01的组件，括号内他等同于方法名，意思是方法名也可以代表组件id，方法返回值相当于xml文件中的bean标签内的class属性的值。配置类本身也是组件。

```java
@Configuration(proxyBeanMethods=true)
public class conf {

    @Bean//注册出来的组件是唯一的，不管调用多少次方法创建的实例他都是同一个实例，spring容器中的单实例
    public User user01(){
        return new User("小李",11);
    }

}
```

proxyBeanMethods表示这个这个方法会不会被代理，如果会的话那么他就是单实例，springboot默认是true，springboot总会检查组件是否在容器有，在容器中检查是否有这个对象，如果有就就不会创建，总是保持这个对象的单实例。如果改为false，多次调用conf的user01方法就会创建多个实例。

配置类有轻量级模式和full模式

- 轻量级不会检查对象是否已经存在，启动快，即proxyBeanMethods=false
- full模式提供组件依赖，就是方便使用容器中的组件

