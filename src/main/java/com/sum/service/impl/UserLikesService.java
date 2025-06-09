package com.sum.service.impl;


import com.sum.domain.entity.UserLikes;
import com.sum.mapper.UserLikesMapper;
import com.sum.service.IUserLikesService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;

/**
 * 【请填写功能名称】Service业务层处理
 *
 */
@Service
public class UserLikesService extends ServiceImpl<UserLikesMapper, UserLikes> implements IUserLikesService {

    @Resource
    private UserLikesMapper userLikesMapper;

}
