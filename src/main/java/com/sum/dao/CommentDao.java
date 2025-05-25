package com.sum.dao;

import com.sum.model.Comment;
import java.sql.SQLException;
import java.util.List;

public interface CommentDao {
    // 创建新评论
    void create(Comment comment) throws SQLException;
    
    // 根据ID获取评论
    Comment getById(int id) throws SQLException;
    
    // 获取笔记的所有评论
    List<Comment> getByNoteId(int noteId) throws SQLException;
    
    // 更新评论
    void update(Comment comment) throws SQLException;
    
    // 删除评论
    void delete(int id) throws SQLException;
    
    // 根据作者获取评论列表
    List<Comment> getByAuthor(String author) throws SQLException;
    
    // 增加点赞数
    void incrementLikes(int commentId) throws SQLException;
    
    // 获取评论数量
    int getCommentCount(int noteId) throws SQLException;
} 