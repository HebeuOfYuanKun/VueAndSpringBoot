package com.yuankun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    protected Integer id;
    protected String Name;
    protected String Code;
    protected String Remark;
    protected Date Created;
    protected Date Updated;
    protected String Statu;

}
