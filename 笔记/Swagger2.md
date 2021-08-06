#### Swagger2

API接口文档，方便前后端分离开发

现在我使用的swagger版本为2.9.2

开启swagger2

```java
@Configuration
@EnableSwagger2
public class DruidConf {
    @Value("${swagger.enable}")//从配置文件中拿到enable的值
    private Boolean enable;
    @Bean
    Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .enable(enable)//是否开始swagger2
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.secondboot"))//扫描包下的才能生成对应的model和api接口
                //.paths()//过滤
                .build();
    }
    private ApiInfo apiInfo(){
        Contact DEFAULT_CONTACT = new Contact("袁坤", "", "574131075@qq.com");//作者信息
        return new ApiInfo("Api Documentation"
                , "Api Documentation"
                , "1.0"
                , "urn:tos"
                , DEFAULT_CONTACT
                , "Apache 2.0"
                , "http://www.apache.org/licenses/LICENSE-2.0"
                , new ArrayList());
    }
}
```

@ApiModel给实体类在接口文档中添加注释，@ApiModelProperty给实体类属性加注释，@ApiOperation给api接口加注释(方法上面)