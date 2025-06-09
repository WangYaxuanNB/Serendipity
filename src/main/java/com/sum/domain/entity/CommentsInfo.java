package com.sum.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CommentsInfo {

    private String avatarUrl;
    private String commenter;
    private Date createTime;
    private String content;
    private Long noteId;
}
