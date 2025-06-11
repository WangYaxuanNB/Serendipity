package com.sum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sum.domain.entity.SensitiveInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 敏感信息Mapper接口
 */
@Mapper
public interface SensitiveInfoMapper extends BaseMapper<SensitiveInfo> {
} 