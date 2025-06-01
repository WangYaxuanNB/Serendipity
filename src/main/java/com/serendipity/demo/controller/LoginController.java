/*
 * Copyright 2025 miyukiWordsworth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.serendipity.demo.controller;

import com.serendipity.demo.dto.LoginParam;
import com.serendipity.demo.model.LoginSysUserTokenVo;
import com.serendipity.demo.service.LoginService;
import com.serendipity.demo.util.ApiResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 登录控制器
 *
 * @author miyuki
 * @date 2025-05-18
 */
@Slf4j
@RestController
@Tag(description = "登录、注销API", name = "LoginController")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录接口
     *
     * @param loginParam 登录参数（用户名和密码）
     * @param response   用于设置响应头 token
     * @param session    当前会话
     * @return 登录成功后返回用户信息和 token
     */
    @PostMapping("/login")
    @Operation(summary = "登录", description = "系统用户登录")
    public ApiResult<LoginSysUserTokenVo> login(@Valid @RequestBody LoginParam loginParam,
                                                HttpServletResponse response,
                                                HttpSession session) throws Exception {
        LoginSysUserTokenVo tokenVo = loginService.login(loginParam);
        return ApiResult.ok(tokenVo, "登录成功");
    }

    /**
     * 用户登出接口
     *
     * @param request 当前请求
     * @param session 当前会话
     * @return 登出成功
     */
    @PostMapping("/logout")
    @Operation(summary = "用户注销", description = "用户登出系统")
    public ApiResult<String> logout(HttpServletRequest request, HttpSession session) throws Exception {
        loginService.logout(request);
        return ApiResult.ok("退出成功");
    }
}
