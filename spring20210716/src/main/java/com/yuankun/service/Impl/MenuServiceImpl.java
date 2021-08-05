package com.yuankun.service.Impl;

import cn.hutool.core.date.DateUtil;
import com.yuankun.mapper.MenuMapper;
import com.yuankun.mapper.RoleMapper;
import com.yuankun.mapper.UserMapper;
import com.yuankun.pojo.Menu;
import com.yuankun.pojo.User;
import com.yuankun.pojo.dto.MenuDto;
import com.yuankun.response.Result;
import com.yuankun.service.AuthorityService;
import com.yuankun.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AuthorityService authorityService;

    @Override
    public Result QueryAlLMenu() {
        List<Menu> menuList=menuMapper.QueryAllMenu();
        List<Menu> menuTree = BuildMenuTree(menuList);


       /* System.out.println("menuTree"+menuTree);

        List<Menu> menuTree1 = BuildMenuTree(menuTree);
        System.out.println("menuTree1"+menuTree1);*/
        Result result=new Result();
        if(menuList.isEmpty()){
            result.setMsg("没有信息");
            result.setObject(null);
            result.setStateCode(50);
        }else{
            result.setMsg("查询成功");
            result.setObject(menuTree);
            result.setStateCode(200);
        }
        return result;
    }
    public List<Menu> BuildMenuTree(List<Menu> menuList){
        List<Menu> findMenuList=new ArrayList<>();
        List<Menu> findMenuList1=new ArrayList<>();

        for (Menu menuFather:menuList) {
            for (Menu menuChild:menuList) {
                if(menuFather.getId()==menuChild.getParentId()){
                    menuFather.getChildren().add(menuChild);
                }
            }
            if(menuFather.getParentId()==0){
                findMenuList.add(menuFather);
            }
            findMenuList1.add(menuFather);

        }
        /*System.out.println("menlist1"+findMenuList1);*/
        return findMenuList;
    }

    @Override
    public Result QueryNav(String username) {
        User user = userMapper.QueryByUserName(username);
        String Authority=authorityService.QueryAuthorityById(user.getId());
        List<Integer> RoleIds = userMapper.QueryRoleIdByUserId(user.getId());
        List<Integer> MenuIds = roleMapper.QueryMenuIdByRoleId(RoleIds);
        List<Menu> menus = menuMapper.QueryMenusByMenuId(MenuIds);
        List<Menu> menuList=BuildMenuTree(menus);
        List<MenuDto> menuDtoList=Convert(menuList);
        Result result=new Result();
        Map<String,Object> map=new HashMap<>();
        String[] AuthorityArray = StringUtils.tokenizeToStringArray(Authority, ",");
        map.put("Authority",AuthorityArray);
        map.put("Nav",menuDtoList);
        result.setMsg("查询成功");
        result.setObject(map);
        result.setStateCode(200);
        return result;
    }

    @Override
    public Result QueryMenuById(Integer id) {
        Menu menu=menuMapper.QueryMenuByMenuId(id);
        Result result=new Result();
        if(menu==null){
            result.setObject(null);
            result.setMsg("查询失败");
            result.setStateCode(200);
        }else{
            result.setObject(menu);
            result.setMsg("查询成功");
            result.setStateCode(200);
        }
        return result;
    }

    public List<MenuDto> Convert(List<Menu> menus){
        List<MenuDto> menuDtos=new ArrayList<>();
        for (Menu menu:menus){
            MenuDto menuDto=new MenuDto();
            menuDto.setTabsName(menu.getName());
            menuDto.setPath(menu.getPath());
            menuDto.setComponent(menu.getComponent());
            menuDto.setIcon(menu.getIcon());
            menuDto.setRouteName(menu.getPerms());
            if(menu.getChildren().size()>0){
                menuDto.setChildren(Convert(menu.getChildren()));
            }
            menuDtos.add(menuDto);
        }
        return menuDtos;
    }

    @Override
    public Result AddMenu(Menu menu) {
        menu.setCreated(DateUtil.date());
        menu.setUpdated(DateUtil.date());
        Integer ResId= menuMapper.AddMenu(menu);
        Result result=new Result();
        if(ResId==null){
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
    public Result UpdateMenu(Menu menu) {
        menu.setUpdated(DateUtil.date());
        Integer ResNum=menuMapper.UpdateMenu(menu);
        Result result=new Result();
        if(ResNum==null){
            result.setStateCode(52);
            result.setObject(null);
            result.setMsg("修改失败");
        }else{
            result.setStateCode(200);
            result.setObject(null);
            result.setMsg("修改成功");
        }
        return result;
    }

    @Override
    public Result DeleteMenu(Integer id) {
        Integer ResNum=menuMapper.DeleteMenu(id);
        Result result=new Result();
        if(ResNum==null){
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
}
