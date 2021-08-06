package com.yuankun.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.yuankun.mapper.MenuMapper;
import com.yuankun.mapper.RoleMapper;
import com.yuankun.mapper.UserMapper;
import com.yuankun.pojo.User;
import com.yuankun.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public String QueryAuthorityById(Integer id) {

        String Authority="";
        List<Integer> RoleIds=userMapper.QueryRoleIdByUserId(id);

        User user=userMapper.QueryById(id);
        Authority= (String) redisTemplate.opsForValue().get("Authority"+user.getUserName());

        if(StrUtil.isBlankOrUndefined(Authority)){
            if(RoleIds==null){
                return null;
            }else{
                List<Integer> MenuIds=roleMapper.QueryMenuIdByRoleId(RoleIds);

                if(MenuIds==null){
                    return null;
                }else{
                    List<String> AuthorityList=menuMapper.QueryPermsByMenuId(MenuIds);
                    Authority=String.join(",",AuthorityList);//sys:menu:list,sys:menu:save....
                    redisTemplate.opsForValue().set("Authority"+user.getUserName(),Authority,60*60, TimeUnit.SECONDS);
                    return Authority;
                }
            }
        }else {
            return Authority;
        }
    }

    @Override
    public void ClearAuthorityByUserName(String UserName) {
        redisTemplate.delete("Authority:"+UserName);
    }

    @Override
    public void ClearAuthorityByRoleId(Integer id) {
        List<Integer> UserIds = roleMapper.QueryUserIdByRoleId(id);
        for(Integer  UserId: UserIds){
            User user=userMapper.QueryById(UserId);
            redisTemplate.delete("Authority:"+user.getUserName());
        }
    }

    @Override
    public void ClearAuthorityByMenuId(Integer id) {
        List<Integer> RoleIds = menuMapper.QueryRoleIdByMenuId(id);
        for(Integer  RoleId: RoleIds){
            List<Integer> UserIds = roleMapper.QueryUserIdByRoleId(RoleId);
            for(Integer  UserId: UserIds){
                User user=userMapper.QueryById(UserId);
                redisTemplate.delete("Authority:"+user.getUserName());
            }
        }

    }

}
