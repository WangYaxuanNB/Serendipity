package com.sum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sum.domain.entity.Mood;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * 心情服务接口
 */
public interface IMoodService extends IService<Mood> {
    
    /**
     * 保存心情记录
     * @param userId 用户ID
     * @param moodText 心情文本
     * @param imageName 图片名称
     * @return 是否保存成功
     */
    boolean saveMoodRecord(Long userId, String moodText, String imageName);
    
    /**
     * 获取指定日期的心情记录
     * @param userId 用户ID
     * @param date 日期
     * @return 心情记录列表
     */
    List<Mood> getMoodsForDate(Long userId, LocalDate date);
    
    /**
     * 获取指定月份的心情记录
     * @param userId 用户ID
     * @param yearMonth 年月
     * @return 心情记录列表
     */
    List<Mood> getMonthlyMoodRecords(Long userId, YearMonth yearMonth);
} 