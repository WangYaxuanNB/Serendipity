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
 * 【请填写功能名称】对象 user
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "user")
public class User implements Serializable {

     @TableField(exist = false)
     private static final long serialVersionUID=1L;

     @TableId(value = "id")
     private Long id;

     @TableField(value = "username" )
     private String username;

     @TableField(value = "password" )
     private String password;

     @TableField(value = "email" )
     private String email;

     @TableField(value = "registration_time" )
     private Date registrationTime;

     @TableField(value = "last_login_time" )
     private Date lastLoginTime;

     @TableField(value = "avatar_url" )
     private String avatarUrl;

}

