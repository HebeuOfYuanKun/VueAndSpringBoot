package com.yuankun.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto implements Serializable {
    protected String TabsName;
    protected String Icon;
    protected String RouteName;
    protected String Component;
    protected String Path;
    protected List<MenuDto> Children=new ArrayList<>();
}
