package com.yuankun.mapper;

import com.yuankun.pojo.Role;
import com.yuankun.pojo.User;
import com.yuankun.pojo.dto.UserTable;
import com.yuankun.security.AccountUser;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    @Select("select * from sys_user where username=#{UserName}")
    User QueryByUserName(String UserName);
    @Select("select id,username,password from sys_user where username=#{UserName}")
    AccountUser QueryAccountUserByUserName(String UserName);
    @Select("select * from sys_user where id=#{id}")
    User QueryById(Integer id);
    @Select("select sys_user_role.role_id from sys_user_role where user_id=#{id}")
    List<Integer> QueryRoleIdByUserId(Integer id);
    @Select("select count(*) from sys_user where username like concat ('%',#{Name},'%')")
    Integer QueryUsersCount(String name);
    List<UserTable> QueryUsers(Map<Object, Object> map);
    @Insert("insert sys_user(username,password,email,city,created,updated,lastlogin,statu) values(" +
            "#{UserName},#{Password},#{Email},#{City},#{Created},#{Updated},#{LastLogin},#{Statu})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    Integer AddUser(User user);
    @Delete("delete from sys_user where id=#{id}")
    Integer DeleteUser(Integer id);
    @Delete("delete from sys_user_role where user_id=#{id}")
    Integer DeleteUserRole(Integer id);
    Integer DeleteUsers(Integer[] ids);
    @Update("update sys_user set username=#{userName},password=#{password},city=#{city},email=#{email},updated=#{Updated},statu=#{Statu}" +
            " where id=#{id}")
    Integer UpdateUser(User user);
    @Select("select role_id from sys_user_role where user_id=#{id}")
    List<Integer> QueryUserRole(Integer id);

    Integer AddUserAuth(Map<String, Object> map);

    Integer DesUserAuth(Map<String, Object> map);
    @Update("update sys_user set password=#{password} where id=#{id}")
    Integer ResetPassword(Map<String, Object> map);
    @Update("update sys_user set password=#{password} where username=#{name}")
    Integer ChangePassword(String name, String password);
}
