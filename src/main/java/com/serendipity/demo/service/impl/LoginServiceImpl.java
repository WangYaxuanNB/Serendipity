package com.serendipity.demo.service.impl;

import com.serendipity.demo.dto.LoginParam;
import com.serendipity.demo.entity.User;
import com.serendipity.demo.model.LoginSysUserTokenVo;
import com.serendipity.demo.repository.UserRepository;
import com.serendipity.demo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录服务实现类
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 用户登录
     *
     * @param loginParam 登录参数（用户名和密码）
     * @return 返回登录成功的用户名和生成的 token
     */
    @Override
    public LoginSysUserTokenVo login(LoginParam loginParam) {
        // 根据用户名查找用户，若不存在则抛出异常
        User user = userRepository.findByUsername(loginParam.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        // 校验密码是否匹配（此处为明文对比，后期建议加密处理）
        if (!user.getPassword().equals(loginParam.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成 token
        String token = ""; // 或者设置为其他默认值

        // 返回用户信息和 token 封装对象
        return new LoginSysUserTokenVo(user.getUsername(), token);
    }

    /**
     * 用户登出
     *
     * @param request 当前请求对象
     */
    @Override
    public void logout(HttpServletRequest request) {
        // 可拓展：例如记录日志、清除缓存、使 token 失效等
    }
}
