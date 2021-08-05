package com.yuankun.conf;


import com.yuankun.mapper.UserMapper;
import com.yuankun.pojo.User;
import com.yuankun.security.AccountUser;
import com.yuankun.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthorityService authorityService;
    @Override
    //用户细节定义
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userMapper.QueryByUserName(username);//通过数据库查询用户是否存在
        if(user==null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        /*System.out.println("管理员"+user+"管理员");*/
        List<GrantedAuthority> list= ProdeceAuthority(user.getId());
        return new AccountUser(user.getId().longValue(),user.getUserName(),user.getPassword(),list);
    }
    //查找权限
    public List<GrantedAuthority> ProdeceAuthority(Integer id){
        String Authority=authorityService.QueryAuthorityById(id);
        System.out.println("Authority----------"+Authority);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(Authority);
    }
}
