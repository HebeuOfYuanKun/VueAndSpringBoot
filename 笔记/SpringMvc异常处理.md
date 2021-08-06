### SpringMvc异常处理

思想：aop面向切片编程

1.采用统一、全局的异常处理

2.把控制层的所有异常都集中在一个地方，业务逻辑和异常处理代码分开，解耦合

两个注解：@ExceptionHandler @ControllerAdvice

步骤：

1.先定义一个普通类作用全部异常处理类

2.在类的上面写@ControllerAdvice，在类中定义方法，方法的上面加上@ExceptionHandler

3.在springmvc配置文件中添加组件扫描器，一个是扫描@Controller注解另一个扫描@ControllerAdvice所在的包名最后声明注解驱动

#### 一般只写其他异常处理