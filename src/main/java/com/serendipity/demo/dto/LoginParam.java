package com.serendipity.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
//接收用户名、密码
@Data
public class LoginParam {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
