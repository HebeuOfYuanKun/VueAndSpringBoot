package com.yuankun.service;

import com.yuankun.pojo.User;
import com.yuankun.response.Result;

import java.util.Map;

public interface UserService {
    Result QueryUserByUserName(String UserName);

    Result QueryUsers(String name, Integer currentPage, Integer size);

    Result AddUser(User user);

    Result DeleteUser(Integer id);

    Result DeleteManyUser(Integer[] ids);

    Result QueryUserById(Integer id);

    Result UpdateUser(User user);

    Result QueryUserRole(Integer id);

    Result UpdateUserAuth(Map<String, Object> objectMap);

    Result ResetPassword(Integer id);

    Result ChangePassword(String name, String currentpassword, String password);
}
