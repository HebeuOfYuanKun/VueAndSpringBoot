##### Mybatis的foreach标签以及其他类似标签的注意事项

```xml
<select id="QueryPermsByMenuId" resultType="String" >
    SELECT perms from sys_menu WHERE id in
    <foreach item="id"  collection="list" open="(" separator="," close=")">#{id}</foreach>
</select>
```

例如上面的一个动态查询，<foreach>标签里面的内容建议不要留太多空格或者换行，强烈建议不要直接粘别人的，很容易出现错误。否则容易出现莫名其妙的错误，在navciate中正常执行的sql语句，而在mybatis中却不行。

###### 错误示范

```xml
<select id="QueryPermsByMenuId" resultType="String" >    
    SELECT perms from sys_menu WHERE id in    <foreach item="id"  collection="list" open="(" separator="," close=")">
    #{id}
    </foreach>
</select>
```

