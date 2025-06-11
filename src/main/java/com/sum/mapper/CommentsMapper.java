package com.sum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sum.domain.entity.Comments;
import com.sum.domain.entity.CommentsInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 */
public interface CommentsMapper extends BaseMapper<Comments> {

    List<CommentsInfo> queryCommentsInfo(Long noteId);
}
