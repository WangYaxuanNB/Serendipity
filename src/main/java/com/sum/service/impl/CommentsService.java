package com.sum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sum.domain.entity.CommentsInfo;
import com.sum.mapper.CommentsMapper;
import com.sum.mapper.NotesMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sum.domain.entity.Comments;
import com.sum.service.ICommentsService;
import javax.annotation.Resource;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 */
@Service
public class CommentsService extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {

    @Resource
    private CommentsMapper commentsMapper;
    @Resource
    private NotesMapper notesMapper;

    @Override
    public void incrementNoteCommentCount(Long id) {
        notesMapper.incrementNoteCommentCount(id);
    }

    @Override
    public void createComment(Comments newComment) {
        this.save(newComment);
    }

    @Override
    public List<Comments> getCommentsByNoteId(Long noteId) {
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comments::getNoteId,noteId);
        return this.list(queryWrapper);
    }

    @Override
    public List<CommentsInfo> queryCommentsInfo(Long noteId) {
        return commentsMapper.queryCommentsInfo(noteId);
    }
}
