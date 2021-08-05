package com.yuankun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenu {
    protected Integer id;
    protected Integer RoleId;
    protected Integer MenuId;
}
