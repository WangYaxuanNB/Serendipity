package com.sum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sum.domain.entity.Record;
import com.sum.domain.entity.RecordInfo;

import java.util.List;


/**
 * 【请填写功能名称】Service接口
 *
 */
public interface IRecordService extends IService<Record>{

    List<RecordInfo> queryRecordInfo(String username);
}
