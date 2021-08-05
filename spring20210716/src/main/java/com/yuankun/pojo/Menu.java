package com.yuankun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    protected Integer id;
    protected Integer ParentId;
    protected String Name;
    protected String Path;
    protected String Perms;
    protected String Component;
    protected Integer Type;
    protected String Icon;
    protected Integer OrderNum;
    protected Date Created;
    protected Date Updated;
    protected Integer Statu;
    protected List<Menu> Children=new ArrayList<>();
}
