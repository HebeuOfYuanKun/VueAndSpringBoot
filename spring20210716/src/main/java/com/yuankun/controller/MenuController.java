package com.yuankun.controller;

import com.yuankun.pojo.Menu;
import com.yuankun.response.Result;
import com.yuankun.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController

public class MenuController {
    @Autowired
    private MenuService menuService;
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @GetMapping("/menu")
    public Result QueryAllMenu(){
        return menuService.QueryAlLMenu();
    }
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @GetMapping("/menu/{id}")
    public Result QueryMenuById(@PathVariable Integer id){
        return menuService.QueryMenuById(id);
    }
    /*@GetMapping("/menu/role/{id}")
    public Result QueryMenuById(@PathVariable Integer id){
        return menuService.QueryMenuById(id);
    }*/
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @GetMapping("/nav")
    public Result QueryNav(Principal principal){
        return menuService.QueryNav(principal.getName());
    }
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @PostMapping("/menu")
    public Result AddMenu(@RequestBody Menu menu){
        return menuService.AddMenu(menu);
    }
    @PreAuthorize("hasAuthority('sys:menu:update')")
    @PutMapping("/menu")
    public Result UpdateMenu(@RequestBody Menu menu){
        return menuService.UpdateMenu(menu);
    }
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @DeleteMapping("/menu/{id}")
    public Result DeleteMenu(@PathVariable Integer id){
        return menuService.DeleteMenu(id);
    }
}
