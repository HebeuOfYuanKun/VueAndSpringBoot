### 自定义修改springboot中jar包版本

用<...-version><...-version/>

自动配置，依赖管理两大功能

##### 自动配置

1. 配置Tomcat
2. 配置springmvc
3. 自动配置web常见功能，例如字符编码功能
4. 默认的包结构：主程序（那个main方法）所在的包下及其子包的文件都可以被识别，如果想要改变默认扫描的包结构在@SpringBootApplication（defaultscanBasePackages=""）@SpringBootApplication 等同于 @SpringBootConfiguration @EnableAutoConfiguration @ComponentScan("com.atguigu.boot")。比如各种包扫描器
5. 各种配置的默认值
6. 按类加载所有自动配置项