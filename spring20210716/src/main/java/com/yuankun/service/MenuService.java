package com.yuankun.service;

import com.yuankun.pojo.Menu;
import com.yuankun.response.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MenuService {
    Result QueryAlLMenu();
    Result QueryNav(String username);
    Result QueryMenuById(Integer id);
    Result AddMenu(Menu menu);

    Result UpdateMenu(Menu menu);

    Result DeleteMenu(Integer id);
}
