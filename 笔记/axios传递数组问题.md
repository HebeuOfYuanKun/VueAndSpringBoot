#### axios传递数组问题

今天在使用`axios`传递数组的时候，不是后台拿不到值，就是前台请求错误，最后使用地址栏拼接做到

浏览器中`Ajax`发送的`http`请求，`get`，`delete`请求不能携带实体，但是HTTP协议规定是可以的。另外`Ajax`请求对于`delete`支持不太友好，所以建议使用地址栏拼接，默认情况下，是没有序列化功能的，会导致拼接异常。

引入`qs`

```javascript
request({url:'/role',

​    method:'delete',

​    params:{

​     ids:this.selectIds

​    },

​     paramsSerializer: params => {//设置序列化

   return qs.stringify(params, { indices: false })

  }

   }).then(res=>{

​    this.delButSta=false

​    this.gettableData()

​    this.$message({

​     message:res.data.msg,

​     type:'success'

​     }) 

   })
```

​    这样后台直接拿数组就可以接收到。