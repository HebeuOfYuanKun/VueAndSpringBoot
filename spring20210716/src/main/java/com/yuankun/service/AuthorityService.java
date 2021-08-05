package com.yuankun.service;

public interface AuthorityService {
    String QueryAuthorityById(Integer id);
    void ClearAuthorityByUserName(String UserName);
    void ClearAuthorityByRoleId(Integer id);
    void ClearAuthorityByMenuId(Integer id);

}
