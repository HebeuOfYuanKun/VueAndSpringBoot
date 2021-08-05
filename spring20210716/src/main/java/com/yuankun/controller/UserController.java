package com.yuankun.controller;

import com.yuankun.pojo.Role;
import com.yuankun.pojo.User;
import com.yuankun.response.Result;
import com.yuankun.service.UserService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/user")
    public Result GetUserInfo(Principal principal){
        return userService.QueryUserByUserName(principal.getName());
    }
    @GetMapping("/users")
    public Result GetUsersInfo(@RequestParam(value = "Name",required = false) String Name
            ,@RequestParam(value = "CurrentPage",required = false,defaultValue = "1") Integer CurrentPage
            ,@RequestParam(value = "Size",required = false,defaultValue = "10") Integer Size){
        return userService.QueryUsers(Name,CurrentPage,Size);
    }
    @PostMapping("/user")
    public Result AddUser(@RequestBody User user){
        //System.out.println(user);
        return userService.AddUser(user);
    }
    @GetMapping("/user/{id}")
    public Result QueryUserById(@PathVariable Integer id){
        return userService.QueryUserById(id);
    }
    @PutMapping("/user")
    public Result UpdateUser(@RequestBody User user){
        System.out.println("-----------"+user);
        return userService.UpdateUser(user);
    }
    @DeleteMapping("/user/{id}")
    public Result DeleteUser(@PathVariable Integer id){
        return userService.DeleteUser(id);
    }
    @DeleteMapping("/user")
    public Result DeleteManyUser(Integer[] ids){
        return userService.DeleteManyUser(ids);
    }
    @GetMapping("/user/role/{id}")
    public Result QueryUserRole(@PathVariable Integer id){
        return userService.QueryUserRole(id);
    }
    @PutMapping("/user/{id}")
    public Result UpdateUserAuth(@PathVariable Integer id, @RequestBody Map<String,Object> map ){
        List<Integer> ids=(List<Integer>) map.get("ids");
        //System.out.println(ids);
        Map<String,Object> objectMap=new HashMap<>();
        objectMap.put("id",id);
        objectMap.put("ids",ids);
        //return null;
       return userService.UpdateUserAuth(objectMap);
    }
    @PutMapping("/user/password/{id}")
    public Result ResetPassword(@PathVariable Integer id){
        return userService.ResetPassword(id);
    }
    @PutMapping("/user/newpassword")
    public Result ChangePassword(Principal principal, String currentpassword, String password){
        System.out.println(currentpassword+"----"+password);
        return  userService.ChangePassword(principal.getName(),currentpassword,password);
    }
}
