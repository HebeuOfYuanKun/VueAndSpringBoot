package com.yuankun.controller;

import com.yuankun.pojo.Role;
import com.yuankun.response.Result;
import com.yuankun.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;
    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/role")
    public Result QueryAllRole(@RequestParam(value = "Name",required = false) String Name,
            @RequestParam(value = "CurrentPage",required = false,defaultValue = "1") Integer CurrentPage,
            @RequestParam(value = "Size", required = false,defaultValue = "10") Integer Size){
        return roleService.QueryAllRole(Name,CurrentPage,Size);
    }
    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/role/{id}")
    public Result QueryRoleById(@PathVariable Integer id){
        return roleService.QueryRoleById(id);
    }
    @PreAuthorize("hasAuthority('sys:role:update')")
    @PutMapping("/role")
    public Result UpdateRole(@RequestBody Role role){
        return roleService.UpdateRole(role);
    }
    @PreAuthorize("hasAuthority('sys:role:save')")
    @PostMapping("/role")
    public Result AddRole(@RequestBody Role role){
        return roleService.AddRole(role);
    }
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @DeleteMapping("/role/{id}")
    public Result DeleteRole(@PathVariable Integer id){
        return roleService.DeleteRole(id);
    }
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @DeleteMapping("/role")
    public Result DeleteManyRole(Integer[] ids){
        return roleService.DeleteManyRole(ids);
    }
    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/role/menu/{id}")
    public Result QueryMenuById(@PathVariable Integer id){
        return roleService.QueryMenuById(id);
    }
    @PreAuthorize("hasAuthority('sys:role:perm')")
    @PutMapping("/role/{id}")
    public Result UpdateRoleAuth(@PathVariable Integer id, @RequestBody Map<String,Object> map){
        List<Integer>  ids=(List<Integer>) map.get("ids");
        Map<String,Object> objectMap=new HashMap<>();
        objectMap.put("id",id);
        objectMap.put("ids",ids);
        return roleService.UpdateRoleAuth(objectMap);
    }
}
