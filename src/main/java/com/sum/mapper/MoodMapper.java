package com.sum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sum.domain.entity.Mood;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 心情记录Mapper接口
 */
@Mapper
@Repository
public interface MoodMapper extends BaseMapper<Mood> {
} 