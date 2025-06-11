package com.sum.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 敏感信息实体类
 */
@Data
@TableName("minganci")
public class SensitiveInfo {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
     * 内容
     */
    private String contentx;
    
    /**
     * 标记时间
     */
    private LocalDateTime bjtime;
    
    /**
     * 处理状态
     */
    private String status;
} 