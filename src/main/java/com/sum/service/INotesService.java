package com.sum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sum.domain.entity.Notes;


/**
 * 【请填写功能名称】Service接口
 *
 */
public interface INotesService extends IService<Notes>{

    void incrementLikes(Long id);

    long getCommentCount(Long id);

    void incrementCommentCount(Long id);
}
