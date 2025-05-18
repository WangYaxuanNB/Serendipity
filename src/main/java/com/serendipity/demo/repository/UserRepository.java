package com.serendipity.demo.repository;

import com.serendipity.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 用户数据访问接口
 * 继承JpaRepository以提供基本的CRUD操作，同时添加自定义查询方法
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 判断指定用户名是否已存在
     * 
     * @param username 用户名
     * @return 存在返回true，不存在返回false
     */
    boolean existsByUsername(String username);
    
    /**
     * 判断指定手机号是否已注册
     * 
     * @param phoneNumber 手机号
     * @return 存在返回true，不存在返回false
     */
    boolean existsByPhoneNumber(String phoneNumber);
    
    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 包含用户的Optional对象，用户不存在时为空
     */
    Optional<User> findByUsername(String username);
}
