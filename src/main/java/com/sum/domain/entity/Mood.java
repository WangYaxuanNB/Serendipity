package com.sum.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 心情记录实体类
 */
@Data
@TableName("moods")
public class Mood implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 心情文本
     */
    private String mood;

    /**
     * 图片名称
     */
    private String fh;

    /**
     * 记录时间
     */
    private LocalDateTime recordTime;

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String imageUrl;
} 