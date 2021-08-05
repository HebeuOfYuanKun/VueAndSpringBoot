package com.yuankun.service.Impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.map.MapUtil;
import com.yuankun.mapper.RoleMapper;
import com.yuankun.mapper.UserMapper;
import com.yuankun.pojo.Role;
import com.yuankun.pojo.User;
import com.yuankun.pojo.dto.UserTable;
import com.yuankun.response.Result;
import com.yuankun.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.security.Principal;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Value("${yuankun.defaultpassword}")
    String password;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public Result QueryUserByUserName(String UserName) {
        User user=userMapper.QueryByUserName(UserName);
        Result result=new Result();
        if(user!=null){
            result.setStateCode(200);
            result.setObject(MapUtil.builder()
                .put("id",user.getId())
                .put("username",user.getUserName())
                .put("avatar",user.getAvatar())
                .build()
            );
            result.setMsg("查询成功");
        }else{
            result.setMsg("查询失败");
            result.setObject(null);
            result.setObject(50);
        }
        return result;
    }

    @Override
    public Result QueryUsers(String name, Integer currentPage, Integer size) {
        Map<Object, Object> map = MapUtil.builder()
                .put("Name", name)
                .put("Start", (currentPage - 1) * size)
                .put("End", currentPage * size)
                .build();
        Integer Count=userMapper.QueryUsersCount(name);
        Result result=new Result();
        if(Count==0){
            result.setObject(null);
            result.setMsg("无结果");
            result.setStateCode(50);
        }else {
            List<UserTable> userList = userMapper.QueryUsers(map);
            //List<UserTable> userTableList=new ArrayList<>();
            for (UserTable userTable:userList){

                System.out.println("-------------------"+userTable);
                List<Integer> RoleIds = userMapper.QueryRoleIdByUserId(userTable.getId());
                List<Role> Roles = roleMapper.QueryRoleByRoleIds(RoleIds);
                List<String> RoleNames=new ArrayList<>();
                for(Role role:Roles){
                    RoleNames.add(role.getName());
                }
                userTable.setUserRole(RoleNames);
                //userTableList.add();
            }
            result.setObject(MapUtil.builder()
                    .put("TotalCount", Count)
                    .put("data", userList)
                    .build());
            result.setMsg("查询成功");
            result.setStateCode(200);
        }
        return result;
    }

    @Override
    public Result AddUser(User user) {
        Result result=new Result();
        user.setCreated(DateUtil.date());
        user.setUpdated(DateUtil.date());
        String password=passwordEncoder.encode(user.getPassword());
        System.out.println(password);
        user.setPassword(password);
        Integer Res=userMapper.AddUser(user);
        if(Res==null){
            result.setStateCode(52);
            result.setObject(null);
            result.setMsg("添加失败");
        }else{
            result.setStateCode(200);
            result.setObject(null);
            result.setMsg("添加成功");
        }
        return result;
    }

    @Override
    public Result DeleteUser(Integer id) {
        Result result=new Result();
        userMapper.DeleteUserRole(id);
        Integer Res=userMapper.DeleteUser(id);
        if(Res==0){
            result.setStateCode(52);
            result.setObject(null);
            result.setMsg("删除失败");
        }else{
            result.setStateCode(200);
            result.setObject(null);
            result.setMsg("删除成功");
        }
        return result;
    }

    @Override
    public Result DeleteManyUser(Integer[] ids) {
        Integer ResId=userMapper.DeleteUsers(ids);
        for(Integer id:ids){
            userMapper.DeleteUserRole(id);
        }
        /* System.out.println("ResId"+ResId); */
        Result result=new Result();
        if(ResId==0){
            result.setMsg("操作失败");
            result.setStateCode(52);
            result.setObject(null);
        }else{
            result.setMsg("操作成功");
            result.setStateCode(200);
            result.setObject(null);
        }
        return result;
    }

    @Override
    public Result QueryUserById(Integer id) {
        User user= userMapper.QueryById(id);
        Result result=new Result();
        if(user==null){
            result.setStateCode(50);
            result.setObject(null);
            result.setMsg("没有符合条件的记录");
        }else{
            result.setStateCode(200);
            result.setObject(user);
            result.setMsg("查询成功");
        }
        return result;
    }

    @Override
    public Result UpdateUser(User user) {
        user.setUpdated(DateUtil.date());
        String password=passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        Integer ResNum=userMapper.UpdateUser(user);
        Result result=new Result();
        if(ResNum==0){
            result.setMsg("操作失败");
            result.setStateCode(52);
            result.setObject(null);
        }else{
            result.setMsg("操作成功");
            result.setStateCode(200);
            result.setObject(null);
        }
        return result;
    }

    @Override
    public Result QueryUserRole(Integer id) {
        List<Integer> RoleIds=userMapper.QueryUserRole(id);
        Result result=new Result();
        if(RoleIds.size()==0){
            result.setMsg("无结果");
            result.setObject(null);
            result.setStateCode(50);
        }else{
            result.setMsg("查询成功");
            result.setObject(RoleIds);
            result.setStateCode(200);
        }
        return result;
    }

    @Override
    public Result UpdateUserAuth(Map<String, Object> objectMap) {
        Integer id = (Integer) objectMap.get("id");
        Result result=new Result();
        /*List<Integer> RoleIds=new ArrayList<>();
        RoleIds.add(id);*/
        List<Integer> ResIds= userMapper.QueryRoleIdByUserId(id);
        List<Integer> RoleIds= (List<Integer>) objectMap.get("ids");
        List<Integer> DiffAddIds=new ArrayList<>();
        for(Integer RoleId:RoleIds){
            if(!ResIds.contains(RoleId)){
                DiffAddIds.add(RoleId);
            }
        }
        //System.out.println(DiffAddIds);
        if(DiffAddIds.size()>0){ //如果修改的数据增加了
            Map<String,Object> map=new HashMap<>();
            map.put("id",id);
            map.put("ids",DiffAddIds);
            Integer Res= userMapper.AddUserAuth(map);
            if(Res!=0){
                result.setMsg("操作成功");
                result.setStateCode(200);
                result.setObject(null);
            }
        }
        List<Integer> DiffDesIds=new ArrayList<>();
        for(Integer RoleId:ResIds){
            if(!RoleIds.contains(RoleId)){
                DiffDesIds.add(RoleId);
            }
        }
        if(DiffDesIds.size()>0){ //如果修改的数据减少了
            Map<String,Object> map=new HashMap<>();
            map.put("id",id);
            map.put("ids",DiffDesIds);
            Integer Res= userMapper.DesUserAuth(map);
            if(Res!=0){
                result.setMsg("操作成功");
                result.setStateCode(200);
                result.setObject(null);
            }
        }
        if(DiffAddIds.size()==0&&DiffDesIds.size()==0){
            result.setMsg("没有进行有效操作");
            result.setStateCode(200);
            result.setObject(null);
        }
        return result;
    }

    @Override
    public Result ResetPassword(Integer id) {
        Result result=new Result();
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        String encode = passwordEncoder.encode(password);
        map.put("password",encode);
        Integer Res=userMapper.ResetPassword(map);
        if(Res==0){
            result.setMsg("修改失败");
            result.setObject(null);
            result.setStateCode(50);
        }else{
            result.setMsg("修改成功");
            result.setObject(null);
            result.setStateCode(200);
        }
        return result;
    }

    @Override
    public Result ChangePassword(String name, String currentpassword, String password) {
        User user=userMapper.QueryByUserName(name);
        Result result=new Result();
        if(user==null){
            result.setMsg("用户名不存在");
            result.setObject(null);
            result.setStateCode(50);
            return result;
        }else {
            if (passwordEncoder.matches(currentpassword,user.getPassword())) {
                Integer Res = userMapper.ChangePassword(name, passwordEncoder.encode(password));
                if (Res == 0) {
                    result.setMsg("修改失败");
                    result.setObject(null);
                    result.setStateCode(52);
                    return result;
                } else {
                    result.setMsg("修改成功");
                    result.setObject(null);
                    result.setStateCode(200);
                    return result;
                }
            } else {
                result.setMsg("原密码错误");
                result.setObject(null);
                result.setStateCode(52);
                return result;
            }
        }
    }
}
