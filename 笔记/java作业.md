### 一、数据库表结构

1.管理员表

![image-20210703213140238](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703213140238.png)

2.菜单表

![image-20210703213233034](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703213233034.png)

### 二、项目简单介绍

​	前端框架layui，后端使用spring+springmvc+mybatis框架实现，数据库使用mysql，连接池使用阿里巴巴的Druid。主要是饭店点菜管理系统，能增删改查菜品信息，能对客户订单进行管理。有全局拦截器，主要是通过cookie进行拦截，对非登录的请求进行拦截处理，如果没有cookie重定向到登录界面。

​	项目结构主要分为java（后端），po包主要有实体类，service处理逻辑，controller控制器，dao包进行数据库操作，test用于测试，util工具类，handler拦截器。webApp（前端）还有一些配置文件。

### 三、登录

##### 1.登录界面

​	点击小眼睛功能实现密码的明暗文转换

![image-20210703213432462](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703213432462.png)

![image-20210703213456092](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703213456092.png)

​	使用错误验证码进行登录

<img src="C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703213559089.png" alt="image-20210703213559089" style="zoom:80%;" />

​	使用错误用户名进行登录

![image-20210703213644192](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703213644192.png)

​	使用错误密码进行登录

![image-20210703213733462](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703213733462.png)

​	登录成功后，跳转到后台管理界面

#### 2.代码实现	

​	前端提交数据方式ajax提交

```javascript
form.on('submit(LAY-user-login-submit)', function(data) {
    //请求登入接口
    $.ajax({
        url:'AdminController/Login.do',
        type:'POST',
        dataType:"json",
        /*contentType:"application/json;charset=utf-8",*/
        data:(data.field),
        success:function (succeed) {
            if(succeed.statusCode=="200"){
                layer.msg(succeed.msg,{offset:'0px'});
                location.href="Admin/LoginSucceed.jsp"
            }
            if(succeed.statusCode=="300"){
                layer.msg(succeed.msg,{offset:'0px'});
            }
        },
    })
});
```

​	后端数据接收

```java
@RequestMapping(value = "/Login.do")
@ResponseBody
public ResultResponse Login(@RequestParam("AdminName") String AdminName, @RequestParam("AdminPassword")String AdminPassword,String remember, HttpServletResponse response, HttpServletRequest request){
    ResultResponse loginResultResponse=new ResultResponse();
    loginResultResponse = adminService.FindAdminByName(AdminName, AdminPassword,remember,request,response);
    return loginResultResponse;
}
```

​	service层

```java
@Resource
private AdminDao adminDao;
public ResultResponse FindAdminByName(String adminName, String adminPassword, String remember, HttpServletRequest request, HttpServletResponse response) {
    Admin admin=adminDao.SelectAdminByName(adminName);
    ResultResponse loginResultResponse=new ResultResponse();
    if(!CodeUtil.checkVerifyCode(request)){//判断验证码是否正确
        loginResultResponse.setStatusCode("300");
        loginResultResponse.setObject(null);
        loginResultResponse.setMsg("验证码错误");
    }else{
        if(admin==null){//判断用户是否存在
            loginResultResponse.setStatusCode("300");
            loginResultResponse.setMsg("用户名不存在！");
            loginResultResponse.setObject(null);
        }else if(!admin.getAdminPassword().equals(adminPassword)){ //判断密码是否正确
            loginResultResponse.setStatusCode("300");
            loginResultResponse.setMsg("密码错误!");
            loginResultResponse.setObject(null);
        }else if(remember!=null){ //判断是否保存密码
            Cookie cookie1=new Cookie("adminname",adminName);
            Cookie cookie2=new Cookie("adminpassword",adminPassword);
            cookie1.setPath(request.getContextPath());
            cookie2.setPath(request.getContextPath());
            cookie1.setMaxAge(3*24*60*60);
            cookie2.setMaxAge(3*24*60*60);
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            loginResultResponse.setStatusCode("200");
            loginResultResponse.setMsg("登录成功!");
            loginResultResponse.setObject(admin);
        }else{
            loginResultResponse.setStatusCode("200");
            loginResultResponse.setMsg("登录成功!");
            loginResultResponse.setObject(admin);
        }
    }
    return loginResultResponse;
}
```

​	dao层的映射文件

```xml
<mapper namespace="com.yuankun.dao.AdminDao">
    <select id="SelectAdminByName" resultType="com.yuankun.po.Admin" parameterType="String">
        select * from admin where AdminName=#{adminName}
    </select>
    <insert id="InsertAdmin" parameterType="Admin" >
        <selectKey keyProperty="id" order="AFTER" resultType="java.lang.Integer">
            select last_insert_id()
        </selectKey>
        insert admin(AdminName,AdminPassword) values (#{AdminName},#{AdminPassword})
    </insert>
</mapper>
```

​	登录响应封装类

```java
protected String StatusCode;//状态码
protected String Msg; //状态信息
protected Object object;//封装对象
```

​	验证码生成类，在web.xml文件中配置相关信息

```java
public static boolean checkVerifyCode(HttpServletRequest request){
    String verifyCodeExpected = (String)request.getSession().getAttribute(
            com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);//获取正确的字符串
    String verifyCodeActual = request.getParameter("verifyCodeActual");//获取得到的字符串
    if(verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)){
        return false;
    }
    return true;
}
```

```xml
<!-- 验证码相关属性的配置 -->
<servlet>
    <servlet-name>Kaptcha</servlet-name>
    <servlet-class>com.google.code.kaptcha.servlet.KaptchaServlet</servlet-class>
    <!-- 定义 Kaptcha 的样式 -->
    <!-- 是否有边框 -->
    <init-param>
        <param-name>kaptcha.border</param-name>
        <param-value>no</param-value>
    </init-param>
    <!-- 字体颜色 -->
    <init-param>
        <param-name>kaptcha.textproducer.font.color</param-name>
        <param-value>red</param-value>
    </init-param>
    <!-- 图片宽度 -->
    <init-param>
        <param-name>kaptcha.image.width</param-name>
        <param-value>130</param-value>
    </init-param>
    <!-- 图片高度 -->
    <init-param>
        <param-name>kaptcha.image.height</param-name>
        <param-value>48</param-value>
    </init-param>
    <!-- 使用哪些字符生成验证码 -->
    <init-param>
        <param-name>kaptcha.textproducer.char.string</param-name>
        <param-value>ACDEFHKPRSTWX345975</param-value>
    </init-param>
</servlet>
```

### 四、后台管理界面

#### 1.界面

​	没有进行任何条件查询

![image-20210703215400721](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703215400721.png)

​	进行精确查询，只输入菜品名

![image-20210703215451863](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703215451863.png)

​	进行精确查询，只输入价格（默认查询此价格及以上菜品）

![image-20210703215643405](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703215643405.png)

​	进行精确查询

![image-20210703215905337](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703215905337.png)	

​	进行模糊查询，只输入菜品名

![image-20210703215744457](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703215744457.png)

​	进行模糊查询，只输入价格（默认查询此价格及以上菜品）

![image-20210703215828696](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703215828696.png)

进行模糊查询

![image-20210703220019653](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20210703220019653.png)

#### 2.代码

​	获取全部数据并进行分页显示

```javascript
table.render({
  elem : '#test1',
  url : '../../FoodController/FindAllMenuPage.do'//API接口
  ,response: {
      statusName: 'statusCode' //规定数据状态的字段名称，默认：code
      ,statusCode: '200' //规定成功的状态码，默认：0
      ,msgName: 'msg' //规定状态信息的字段名称，默认：msg
      ,countName: 'count' //规定数据总数的字段名称，默认：count
      ,dataName: 'object' //规定数据列表的字段名称，默认：data
  },
toolbar : '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
  ,
  defaultToolbar : [ 'filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
    title : '提示',
    layEvent : 'LAYTABLE_TIPS',
    icon : 'layui-icon-tips'
  } ],
  title : '菜品数据',
  limits : [ 3, 5, 8, 10, 15 ],
  cols : [ [ {
    type : 'checkbox',
    fixed : 'left'
  }, {
    field : 'id',
    width : 80,
    title : 'ID',
    sort : true
  }, {
    field : 'foodName',
    width : 200,
    title : '菜品名'
  }, {
    field : 'foodPrice',
    width : 200,
    title : '菜品价格'
  }, {
    field : 'foodWeight',
    width : 200,
    title : '菜品重量',
    sort : true
  }, {
    field : 'isMain',
    width : 200,
    title : '是否为主食',
  }, {
    field : 'foodCost',
    width : 200,
    title : '成本',
    sort : true
  }, {
    fixed : 'right',
    title : '操作',
    toolbar : '#barDemo',
    width : 150
  } ] ],
  page : true
});
```

​	进行精确查询

```javascript
$(document).on('click','#searchbyexa',function (data){
  //获取搜索文本框对象
  var FoodName=$("#FoodName");
  var FoodPrice=$("#FoodPrice");
  table.reload('test1',{
    url:'../../FoodController/FindMenuPageByExa.do',//api接口
    where:{
      FoodName:FoodName.val(),//额外参数
      FoodPrice:FoodPrice.val(),
    }
    ,page:{
      curr:1//从第一页开始条件查询
    }
  })
})
```

​	进行模糊查询

```javascript
$(document).on('click','#searchbyvag',function (data){
  //获取搜索文本框对象
  var FoodName=$("#FoodName");
  var FoodPrice=$("#FoodPrice");
  table.reload('test1',{
    url:'../../FoodController/FindMenuPageByVag.do',//api接口
    where:{
      FoodName:FoodName.val(),//额外参数
      FoodPrice:FoodPrice.val(),
    }
    ,page:{
      curr:1//从第一页开始条件查询
    }
  })
})
```

​	精确查询Controller层，调用service层方法进行实现，返回MenuResultResponse对象的json格式。

```java
@RequestMapping("/FindMenuPageByExa.do")
@ResponseBody
public MenuResultResponse FindMenuPageByExa(String FoodName,Double FoodPrice, int page, int limit){
    MenuResultResponse menuResultResponse=menuService.FindMenuPageByExa(FoodName,FoodPrice,page,limit);
    return menuResultResponse;
}
```

​	模糊查询Controller层，调用service层方法进行实现，返回MenuResultResponse对象的json格式。

```java
@RequestMapping("/FindMenuPageByVag.do")
@ResponseBody
public MenuResultResponse FindMenuPageByVag(String FoodName,Double FoodPrice,int page,int limit){
    MenuResultResponse menuResultResponse=menuService.FindMenuPageByVag(FoodName,FoodPrice,page,limit);
    return menuResultResponse;
}
```

​	service层的精确查询，利用pagebean包装类，可以得到后台需要的开始标记的结束标记，利用map进行储存不同数据类型的键值对，以便传给我们的dao包类的映射文件，把查询的到的信息封装到MenuResultResponse类中。

```java
public MenuResultResponse FindMenuPageByExa(String FoodName, Double FoodPrice, int page, int limit){
    PageBean<Menu> pageBean = new PageBean<Menu>(); //pagebean初始化
    pageBean.setCurrentPage(page);
    pageBean.setPageSize(limit);
    MenuResultResponse menuResultResponse=new MenuResultResponse();
    int start=pageBean.getStartRow();  //得到起始记录标记
    int end=pageBean.getEndRow();     //得到结束记录标记
    Map<String,Object> map = new HashMap<>();//利用map方式把开始标记和结束标记传入到SQL中
    map.put("FoodName",FoodName);
    map.put("FoodPrice",FoodPrice);
    int count=menuDao.FindMenuCountByExa(map);
    map.put("start",start);
    map.put("end",end-start);
    List<Menu> menuList=null;
    menuList=menuDao.FindMenuPageByExa(map);
    pageBean.setTotalCount(count);
    if(menuList==null){
        menuResultResponse.setMsg("菜品不存在！");
        menuResultResponse.setStatusCode("300");
        menuResultResponse.setCount("0");
        menuResultResponse.setObject(null);
    }else {
        menuResultResponse.setMsg("查询成功");
        menuResultResponse.setStatusCode("200");
        menuResultResponse.setCount(String.valueOf(count));
        menuResultResponse.setObject(menuList);
    }
    return menuResultResponse;
}
```

​	模糊查询，道理同上。

```java
public MenuResultResponse FindMenuPageByVag(String FoodName, Double FoodPrice, int page, int limit){
    PageBean<Menu> pageBean = new PageBean<Menu>(); //pagebean初始化
    pageBean.setCurrentPage(page);
    pageBean.setPageSize(limit);
    MenuResultResponse menuResultResponse=new MenuResultResponse();
    int start=pageBean.getStartRow();  //得到起始记录标记
    int end=pageBean.getEndRow();     //得到结束记录标记
    Map<String,Object> map = new HashMap<>();//利用map方式把开始标记和结束标记传入到SQL中
    map.put("FoodName",FoodName);
    map.put("FoodPrice",FoodPrice);
    int count=menuDao.FindMenuCountByVag(map);
    map.put("start",start);
    map.put("end",end-start);
    List<Menu> menuList=null;
    menuList=menuDao.FindMenuPageByVag(map);
    pageBean.setTotalCount(count);
    if(menuList==null){
        menuResultResponse.setMsg("菜品不存在！");
        menuResultResponse.setStatusCode("300");
        menuResultResponse.setCount("0");
        menuResultResponse.setObject(null);
    }else {
        menuResultResponse.setMsg("查询成功");
        menuResultResponse.setStatusCode("200");
        menuResultResponse.setCount(String.valueOf(count));
        menuResultResponse.setObject(menuList);
    }
    return menuResultResponse;
}
```

SQL语句，精确查询

```xml
<select id="FindMenuPageByExa" parameterType="java.util.Map" resultType="com.yuankun.po.Menu">
    select * from menu
    <where>
        <if test="FoodName!=null and FoodName!=''">//都不为空才进行拼接
            and FoodName = #{FoodName}
        </if>
        <if test="FoodPrice!=null">
            and FoodPrice >=#{FoodPrice}
        </if>
    </where>
    order by FoodName limit #{start},#{end}
</select>
```

模糊查询

```xml
<select id="FindMenuPageByVag" parameterType="java.util.Map" resultType="com.yuankun.po.Menu">
    select * from menu
    <where>
        <if test="FoodName!=null and FoodName!=''">
            and FoodName like  concat ('%',#{FoodName},'%')
        </if>
        <if test="FoodPrice!=null">
            and FoodPrice >=#{FoodPrice}
        </if>
    </where>
    order by FoodName limit #{start},#{end}
</select>
```

​	拦截器，主要是通过cookie形式进行拦截。

```java
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    Cookie[] cookies = request.getCookies();
    String url=request.getRequestURI();
    if(url.indexOf("Login.do") != -1){
        return true;
    }
    if(cookies==null){
        response.sendRedirect("../Admin/Login.jsp?message=nologin");
        return false;
    }else{
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("adminname")){
                return true;
            }
        }
    }
    response.sendRedirect("../Admin/Login.jsp"+"?message=nologin");
    return false;
}
```

### 五、个人总结

1.对于javaweb的学习还任重道远，许多前端的知识好多还不甚了解，比如ajax异步提交，花费了我很多的时间去学习，而现在却还不太会使用，希望自己继续加油学习。

2.对于mysql的多表查询还是不太会使用，自己还有很大的不足。

3.Tomcat自带defaultservlet，用于处理静态资源的访问，当我让中央调度器的范围变成全局范围后，许多的静态资源出现了404错误，那是因为没有对应的servlet进行处理，后来加入了一些设置，自己的中央调度器放行了对静态资源的访问。

4.用cookie进行拦截的方法有很大的弊端，就是用户必须设置保存密码，要不然cookie无法保存，现在普遍用token进行请求拦截，所以希望自己尽快学习非关系型数据库，完成以token形式的请求拦截。

5.ajax如果以post请求提交，如果指定了contentType，那后端必须用@requestbody进行接收，即以对象的形式进行接收，不可以单个接受。不指定则可以。

6.拦截器进行设置时，对于两个response会报500错误，所以当条件判断后必须执行一个return，如果不执行，那不能同时执行两个response。