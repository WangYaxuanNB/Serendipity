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
     * 判断指定邮箱是否已注册
     * 
     * @param email 邮箱
     * @return 存在返回true，不存在返回false
     */
    boolean existsByEmail(String email);
    
    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 包含用户的Optional对象，用户不存在时为空
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱
     * @return 包含用户的Optional对象，用户不存在时为空
     */
    Optional<User> findByEmail(String email);
}
