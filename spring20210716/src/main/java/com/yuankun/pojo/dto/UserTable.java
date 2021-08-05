package com.yuankun.pojo.dto;

import com.yuankun.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTable extends User {
    protected List<String> UserRole;

    @Override
    public String toString() {
        return "UserTable{" +
                "id=" + id +
                ", UserName='" + UserName + '\'' +
                ", Password='" + Password + '\'' +
                ", Avatar='" + Avatar + '\'' +
                ", Email='" + Email + '\'' +
                ", City='" + City + '\'' +
                ", Created=" + Created +
                ", Updated=" + Updated +
                ", LastLogin=" + LastLogin +
                ", Statu=" + Statu +
                ", UserRole=" + UserRole +
                '}';
    }
}
