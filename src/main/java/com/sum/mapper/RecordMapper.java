package com.sum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sum.domain.entity.Record;
import com.sum.domain.entity.RecordInfo;

import java.util.List;


/**
 * 【请填写功能名称】Mapper接口
 *
 */
public interface RecordMapper extends BaseMapper<Record> {
    List<RecordInfo> queryRecordInfo(String username);
}
