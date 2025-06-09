package com.sum.domain.entity;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sum.annotations.ViewHeader;
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
@TableName(value = "comments")
public class Comments implements Serializable {

     @TableField(exist = false)
     private static final long serialVersionUID=1L;

     @TableId(value = "id")
     private Long id;

     @ViewHeader("评论者")
     @TableField(value = "author" )
     private String author;

     @ViewHeader("评论时间")
     @TableField(exist = false)
     private String createTimeStr;

     @ViewHeader("评论内容")
     @TableField(value = "content" )
     private String content;

     @TableField(value = "create_time" )
     private Date createTime;

     @TableField(value = "likes" )
     private Long likes;

     @TableField(value = "note_id" )
     private Long noteId;

     public String getCreateTimeStr() {
          if (ObjectUtil.isNull(this.getCreateTime())) {
               return "";
          }
          return DateUtil.format(this.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN);
     }
}

