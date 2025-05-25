package com.sum.dao;

import com.sum.model.Note;
import java.util.List;

public interface NoteDao {
    // 创建新笔记
    Note create(Note note);
    
    // 根据ID获取笔记
    Note getById(int id);
    
    // 获取所有笔记
    List<Note> getAll();
    
    // 更新笔记
    void update(Note note);
    
    // 删除笔记
    void delete(int id);
    
    // 根据作者获取笔记列表
    List<Note> getByAuthor(String author);
    
    // 获取热门笔记（按点赞数排序）
    List<Note> getHotNotes(int limit);
    
    // 增加点赞数
    void incrementLikes(int id);
    
    // 增加评论数
    void incrementCommentCount(int id);
} 