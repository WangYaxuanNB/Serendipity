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
 * 【请填写功能名称】对象 user_likes
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "user_likes")
public class UserLikes implements Serializable {

     @TableField(exist = false)
     private static final long serialVersionUID=1L;

     @TableId(value = "id")
     private Long id;

     @TableField(value = "user_name" )
     private String userName;

     @TableField(value = "note_id" )
     private Long noteId;

     @TableField(value = "comment_id" )
     private Long commentId;

     @TableField(value = "created_at" )
     private Date createdAt;

}

