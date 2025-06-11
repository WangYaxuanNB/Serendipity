package com.sum.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.sum.domain.entity.Notes;
import com.sum.mapper.NotesMapper;
import com.sum.service.INotesService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;

/**
 * 【请填写功能名称】Service业务层处理
 *
 */
@Service
public class NotesService extends ServiceImpl<NotesMapper, Notes> implements INotesService {

    @Resource
    private NotesMapper notesMapper;

    @Override
    public void incrementLikes(Long id) {
        notesMapper.incrementLikes(id);
    }

    @Override
    public long getCommentCount(Long id) {
        Notes notes = this.getById(id);
        return ObjectUtil.isNull(notes) ? 0:notes.getCommentCount();
    }

    @Override
    public void incrementCommentCount(Long id) {
        notesMapper.incrementNoteCommentCount(id);
    }
}
