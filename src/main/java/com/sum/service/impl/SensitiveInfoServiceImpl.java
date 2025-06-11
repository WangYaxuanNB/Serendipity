package com.sum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sum.domain.entity.SensitiveInfo;
import com.sum.mapper.SensitiveInfoMapper;
import com.sum.service.ISensitiveInfoService;
import org.springframework.stereotype.Service;

/**
 * 敏感信息服务实现类
 */
@Service
public class SensitiveInfoServiceImpl extends ServiceImpl<SensitiveInfoMapper, SensitiveInfo> implements ISensitiveInfoService {
} 