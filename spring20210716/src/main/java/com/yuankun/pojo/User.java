package com.yuankun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    protected Integer id;
    protected String UserName;
    protected String Password;
    protected String Avatar;
    protected String Email;
    protected String City;
    protected Date Created;
    protected Date Updated;
    protected Date LastLogin;
    protected Integer Statu;

}
