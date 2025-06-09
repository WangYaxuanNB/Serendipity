package com.sum.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 【请填写功能名称】对象 comments
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "record")
public class Record implements Serializable {

     @TableField(exist = false)
     private static final long serialVersionUID=1L;

     @TableId(value = "id")
     private Long id;

     @TableField(value = "note_id" )
     private Long noteId;

     @TableField(value = "author" )
     private String author;

     @TableField(value = "type")
     private Integer type;

     @TableField(value = "content" )
     private String content;

     @TableField(value = "create_time" )
     private Date createTime;

}

