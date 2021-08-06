package com.yuankun.service.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.yuankun.mapper.RoleMapper;
import com.yuankun.pojo.Role;
import com.yuankun.response.Result;
import com.yuankun.service.RoleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public Result QueryAllRole(String name, Integer currentPage, Integer size) {
        Map<String,Object> map=new HashMap<>();
        map.put("Name",name);
        map.put("start",(currentPage-1)*size);
        map.put("end",currentPage*size);
        Result result=new Result();
        Map<Object, Object> ResMap = MapUtil.builder()
                .put("data",null)
                .put("TotalCount", 0)
                .build();
        Integer TotalCount=roleMapper.QueryAllRoleCount(name);
        if(TotalCount==0){
            result.setMsg("没有符合条件的记录");
            ResMap.replace("TotalCount",0);
            result.setObject(ResMap);
            result.setStateCode(50);
            return result;
        }else{
            List<Role> roleList=roleMapper.QueryAllRole(map);
            if(roleList==null){
                result.setMsg("没有符合条件的记录");
                result.setObject(ResMap);
                result.setStateCode(50);
            }else{
                result.setMsg("查询成功");
                ResMap.replace("data",roleList);
                ResMap.replace("TotalCount",TotalCount);
                result.setObject(ResMap);
                result.setStateCode(200);
            }
            return result;
        }

    }

    @Override
    public Result QueryRoleById(Integer id) {
        Role role= roleMapper.QueryRoleById(id);
        Result result=new Result();
        if(role==null){
            result.setStateCode(50);
            result.setObject(null);
            result.setMsg("没有符合条件的记录");
        }else{
            result.setStateCode(200);
            result.setObject(role);
            result.setMsg("查询成功");
        }
        return result;
    }

    @Override
    public Result UpdateRole(Role role) {
        role.setUpdated(DateUtil.date());
        Integer ResNum=roleMapper.UpdateRole(role);
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
    public Result AddRole(Role role) {
        role.setUpdated(DateUtil.date());
        role.setCreated(DateUtil.date());
        Integer ResId=roleMapper.AddRole(role);
        Result result=new Result();
        if(ResId==null){
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
    public Result DeleteRole(Integer id) {
        Integer ResId=roleMapper.DeleteRole(id);
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
    public Result DeleteManyRole( Integer[] ids) {

        Integer ResId=roleMapper.DeleteRoles(ids);
        //System.out.println("ResId"+ResId);
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
    public Result QueryMenuById(Integer id) {
        List<Integer> ResId=roleMapper.QueryMenuById(id);
        //System.out.println("ResId"+ResId);
        Result result=new Result();
        if(ResId.size()==0){
            result.setMsg("操作失败");
            result.setStateCode(52);
            result.setObject(null);
        }else{
            result.setMsg("操作成功");
            result.setStateCode(200);
            result.setObject(ResId);
        }
        return result;
    }

    @Override
    public Result UpdateRoleAuth(Map<String, Object> objectMap) {
        Integer id = (Integer) objectMap.get("id");
        Result result=new Result();
        List<Integer> RoleIds=new ArrayList<>();
        RoleIds.add(id);
        List<Integer> ResIds= roleMapper.QueryMenuIdByRoleId(RoleIds);
        List<Integer> MenuIds= (List<Integer>) objectMap.get("ids");
        List<Integer> DiffAddIds=new ArrayList<>();
        for(Integer MenuId:MenuIds){
            if(!ResIds.contains(MenuId)){
                DiffAddIds.add(MenuId);
            }
        }
        //System.out.println(DiffAddIds);
        if(DiffAddIds.size()>0){ //如果修改的数据增加了
            Map<String,Object> map=new HashMap<>();
            map.put("id",id);
            map.put("ids",DiffAddIds);
            Integer Res= roleMapper.AddRoleAuth(map);
            if(Res!=0){
                result.setMsg("操作成功");
                result.setStateCode(200);
                result.setObject(null);
            }
        }
        List<Integer> DiffDesIds=new ArrayList<>();
        for(Integer MenuId:ResIds){
            if(!MenuIds.contains(MenuId)){
                DiffDesIds.add(MenuId);
            }
        }
        if(DiffDesIds.size()>0){ //如果修改的数据减少了
            Map<String,Object> map=new HashMap<>();
            map.put("id",id);
            map.put("ids",DiffDesIds);
            Integer Res= roleMapper.DesRoleAuth(map);
            if(Res!=0){
                result.setMsg("操作成功");
                result.setStateCode(200);
                result.setObject(null);
            }
        }
        //System.out.println(DiffDesIds);
        //roleMapper.UpdateRoleAuth();
        return result;
    }
}
