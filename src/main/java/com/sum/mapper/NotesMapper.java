package com.sum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sum.domain.entity.Notes;

/**
 * 【请填写功能名称】Mapper接口
 *
 */
public interface NotesMapper extends BaseMapper<Notes> {

    void incrementLikes(Long id);

    void incrementNoteCommentCount(Long id);
}
