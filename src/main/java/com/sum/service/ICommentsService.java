package com.sum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sum.domain.entity.Comments;
import com.sum.domain.entity.CommentsInfo;

import java.util.List;


/**
 * 【请填写功能名称】Service接口
 *
 */
public interface ICommentsService extends IService<Comments>{

    void incrementNoteCommentCount(Long id);

    void createComment(Comments newComment);

    List<Comments> getCommentsByNoteId(Long id);

    List<CommentsInfo> queryCommentsInfo(Long id);
}
