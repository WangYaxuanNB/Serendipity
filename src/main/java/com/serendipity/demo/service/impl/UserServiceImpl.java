package com.serendipity.demo.service.impl;

import com.serendipity.demo.entity.User;
import com.serendipity.demo.repository.UserRepository;
import com.serendipity.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 * 实现用户相关的业务逻辑，如注册、登录等
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 用户注册实现
     * 包含多项业务规则验证：
     * 1. 用户名唯一性检查
     * 2. 手机号唯一性检查
     * 3. 手机号格式验证
     * 4. 密码强度验证
     * 
     * @param user 用户实体对象
     * @throws RuntimeException 当验证失败时抛出异常
     */
    @Override
    public void register(User user) {
        // 1. 用户名查重
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 手机号查重
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new RuntimeException("手机号已注册");
        }

        // 3. 手机号格式验证（中国大陆手机号）
        if (!user.getPhoneNumber().matches("^1[3-9]\\d{9}$")) {
            throw new RuntimeException("手机号格式不正确，应为合法的中国手机号");
        }

        // 4. 密码强度验证
        String password = user.getPassword();

        if (password.length() < 6) {
            throw new RuntimeException("密码长度必须不少于6位");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new RuntimeException("密码必须包含至少一个大写字母");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new RuntimeException("密码必须包含至少一个小写字母");
        }
        if (!password.matches(".*\\d.*")) {
            throw new RuntimeException("密码必须包含至少一个数字");
        }

        // 5. 保存用户
        userRepository.save(user);
    }
    
    /**
     * 用户登录实现
     * 验证用户名和密码是否匹配
     * 
     * @param username 用户名
     * @param password 密码
     * @return 成功则返回用户实体
     * @throws RuntimeException 当用户名不存在或密码错误时抛出
     */
    @Override
    public User login(String username, String password) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户名不存在"));
        
        // 验证密码
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        
        return user;
    }
}
