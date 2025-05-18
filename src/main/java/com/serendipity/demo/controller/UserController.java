package com.serendipity.demo.controller;

import com.serendipity.demo.entity.User;
import com.serendipity.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 处理用户相关的HTTP请求，包括注册和登录功能
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * 接收客户端发送的用户信息，调用服务层完成注册
     * 
     * @param user 用户实体对象，从请求体中解析
     * @return 返回注册结果，包含成功/失败状态和消息
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.register(user);
            response.put("success", true);
            response.put("message", "注册成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // 捕获运行时异常，通常是业务逻辑验证失败
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 用户登录接口
     * 接收用户名和密码，验证后返回用户信息
     * 
     * @param loginRequest 包含用户名和密码的请求体
     * @return 返回登录结果，成功则包含用户信息
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            
            User user = userService.login(username, password);
            
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // 登录失败处理，如用户名不存在或密码错误
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
