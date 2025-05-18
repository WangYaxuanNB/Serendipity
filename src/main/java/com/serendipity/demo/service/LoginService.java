package com.serendipity.demo.service;

import com.serendipity.demo.dto.LoginParam;
import com.serendipity.demo.model.LoginSysUserTokenVo;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    LoginSysUserTokenVo login(LoginParam loginParam);
    void logout(HttpServletRequest request);
}
