package com.sum.domain.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 【请填写功能名称】对象 notes
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "notes")
public class Notes implements Serializable {

     @TableField(exist = false)
     private static final long serialVersionUID=1L;

     @TableId(value = "id")
     private Long id;

     @TableField(value = "title" )
     private String title;

     @TableField(value = "description" )
     private String description;

     @TableField(value = "author" )
     private String author;

     @TableField(value = "image_url" )
     private String imageUrl;

     @TableField(value = "likes" )
     private Long likes;

     @TableField(value = "comment_count" )
     private Long commentCount;

     @TableField(value = "image_height" )
     private Integer imageHeight;

     @TableField(value = "created_at" )
     private Date createdAt;

}

