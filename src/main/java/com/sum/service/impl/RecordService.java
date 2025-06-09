package com.sum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sum.domain.entity.Record;
import com.sum.domain.entity.RecordInfo;
import com.sum.mapper.RecordMapper;
import com.sum.service.IRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 */
@Service
public class RecordService extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    @Resource
    private RecordMapper recordMapper;

    @Override
    public List<RecordInfo> queryRecordInfo(String username) {
        return recordMapper.queryRecordInfo(username);
    }
}
