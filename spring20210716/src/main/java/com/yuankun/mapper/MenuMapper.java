package com.yuankun.mapper;

import com.yuankun.pojo.Menu;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MenuMapper {
    List<Menu> QueryAllMenu();
    List<String> QueryPermsByMenuId(List<Integer> id);
    @Select("select RoleId from sys_role_menu where MenuId=#{id}")
    List<Integer> QueryRoleIdByMenuId(Integer id);
    List<Menu> QueryMenusByMenuId(List<Integer> id);
    Menu QueryMenuByMenuId(Integer id);
    Integer AddMenu(Menu menu);
    Integer UpdateMenu(Menu menu);
    @Delete("delete from sys_menu where id=#{id}")
    Integer DeleteMenu(Integer id);
}
