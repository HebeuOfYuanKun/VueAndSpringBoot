package com.yuankun.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    protected Integer StateCode;//状态码,50代表查询没有结果||用户名不存在，51代表用户密码错误，52代表操作失败
    protected String Msg;//信息
    protected Object object;
}
