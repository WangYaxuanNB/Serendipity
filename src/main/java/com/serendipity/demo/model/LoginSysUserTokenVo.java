package com.serendipity.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//定义返回值
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginSysUserTokenVo {
    private String username;
    private String token;
}
