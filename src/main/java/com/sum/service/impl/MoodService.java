package com.sum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sum.domain.entity.Mood;
import com.sum.mapper.MoodMapper;
import com.sum.service.IMoodService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

/**
 * 心情服务实现类
 */
@Service
public class MoodService extends ServiceImpl<MoodMapper, Mood> implements IMoodService {

    @Override
    public boolean saveMoodRecord(Long userId, String moodText, String imageName) {
        Mood mood = new Mood();
        mood.setUserId(userId);
        mood.setMood(moodText);
        mood.setFh(imageName);
        mood.setRecordTime(LocalDateTime.now());
        return this.save(mood);
    }

    @Override
    public List<Mood> getMoodsForDate(Long userId, LocalDate date) {
        // 构建查询条件：根据用户ID和日期查询
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        LambdaQueryWrapper<Mood> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Mood::getUserId, userId)
                .ge(Mood::getRecordTime, startOfDay)
                .le(Mood::getRecordTime, endOfDay)
                .orderByDesc(Mood::getRecordTime);
        
        return this.list(queryWrapper);
    }

    @Override
    public List<Mood> getMonthlyMoodRecords(Long userId, YearMonth yearMonth) {
        // 构建查询条件：根据用户ID和月份查询
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        
        LambdaQueryWrapper<Mood> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Mood::getUserId, userId)
                .ge(Mood::getRecordTime, startOfMonth)
                .le(Mood::getRecordTime, endOfMonth)
                .orderByAsc(Mood::getRecordTime);
        
        return this.list(queryWrapper);
    }
} 