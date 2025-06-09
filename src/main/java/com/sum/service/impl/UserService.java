package com.sum.service.impl;


import com.sum.domain.entity.User;
import com.sum.mapper.UserMapper;
import com.sum.service.IUserService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;
/**
 * 【请填写功能名称】Service业务层处理
 *
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

}
