package com.yuankun.service;

import com.yuankun.pojo.Role;
import com.yuankun.response.Result;

import java.util.Map;

public interface RoleService {
    Result QueryAllRole(String name, Integer currentPage, Integer size);

    Result QueryRoleById(Integer id);

    Result UpdateRole(Role role);

    Result AddRole(Role role);

    Result DeleteRole(Integer id);

    Result DeleteManyRole(Integer[] ids);

    Result QueryMenuById(Integer id);

    Result UpdateRoleAuth(Map<String, Object> ids);
}
