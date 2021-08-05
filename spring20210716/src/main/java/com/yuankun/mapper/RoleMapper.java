package com.yuankun.mapper;

import com.yuankun.pojo.Role;
import org.apache.ibatis.annotations.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Mapper
public interface RoleMapper {
    @Select("select * from sys_role where name like concat ('%',#{Name},'%')")
    List<Role> QueryRoleByName(String Name);
    List<Integer> QueryMenuIdByRoleId(List<Integer> id);
    @Select("select user_id from sys_user_role where role_id = #{id}")
    List<Integer> QueryUserIdByRoleId(Integer id);
    List<Role> QueryAllRole(Map<String,Object> map);
    Integer QueryAllRoleCount(String Name);
    @Select("select * from sys_role where id=#{id}")
    Role QueryRoleById(Integer id);
    @Update("update sys_role set name=#{Name},code=#{Code},remark=#{Remark},updated=#{Updated},statu=#{statu}" +
            " where id=#{id}")
    Integer UpdateRole(Role role);
    @Insert("insert sys_role(name,code,remark,created,updated,statu) values(#{Name},#{code}," +
            "#{Remark},#{Created},#{Updated},#{Statu})")
    Integer AddRole(Role role);
    @Delete("delete from sys_role where id=#{id}")
    Integer DeleteRole(Integer id);
    Integer DeleteRoles(Integer[] ids);
    @Select("select MenuId from sys_role_menu where RoleId=#{id}")
    List<Integer> QueryMenuById(Integer id);
    Integer AddRoleAuth(Map<String,Object> map);
    Integer DesRoleAuth(Map<String,Object> map);

    List<Role> QueryRoleByRoleIds(List<Integer> ids);
}
