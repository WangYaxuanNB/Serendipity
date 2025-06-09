package com.sum.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class RecordInfo {

    private String avatarUrl;
    private String imageUrl;
    private String operator;
    private Date createTime;
    private String content;
    private Integer type;
    private String owners;
    private Long noteId;
}
