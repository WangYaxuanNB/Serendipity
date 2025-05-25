package com.sum.dao.impl;

import com.sum.config.DatabaseConfig;
import com.sum.dao.CommentDao;
import com.sum.model.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDaoImpl implements CommentDao {
    
    @Override
    public void create(Comment comment) {
        String sql = "INSERT INTO comments (content, author, author_avatar, create_time, likes, note_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
                    
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, comment.getContent());
            pstmt.setString(2, comment.getAuthor());
            pstmt.setString(3, comment.getAuthorAvatar());
            pstmt.setTimestamp(4, comment.getCreateTime());
            pstmt.setInt(5, comment.getLikes());
            pstmt.setInt(6, comment.getNoteId());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comment.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Comment getById(int id) {
        String sql = "SELECT * FROM comments WHERE id = ?";
        Comment comment = null;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                comment = extractCommentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comment;
    }
    
    @Override
    public List<Comment> getByNoteId(int noteId) {
        String sql = "SELECT * FROM comments WHERE note_id = ? ORDER BY create_time DESC";
        List<Comment> comments = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, noteId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                comments.add(extractCommentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
    
    @Override
    public void update(Comment comment) {
        String sql = "UPDATE comments SET content = ?, author = ?, author_avatar = ?, " +
                    "create_time = ?, likes = ? WHERE id = ?";
                    
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, comment.getContent());
            pstmt.setString(2, comment.getAuthor());
            pstmt.setString(3, comment.getAuthorAvatar());
            pstmt.setTimestamp(4, comment.getCreateTime());
            pstmt.setInt(5, comment.getLikes());
            pstmt.setInt(6, comment.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM comments WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Comment> getByAuthor(String author) {
        String sql = "SELECT * FROM comments WHERE author = ? ORDER BY create_time DESC";
        List<Comment> comments = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, author);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                comments.add(extractCommentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
    
    @Override
    public void incrementLikes(int commentId) {
        String sql = "UPDATE comments SET likes = likes + 1 WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, commentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public int getCommentCount(int noteId) {
        String sql = "SELECT COUNT(*) FROM comments WHERE note_id = ?";
        int count = 0;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, noteId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    private Comment extractCommentFromResultSet(ResultSet rs) throws SQLException {
        Comment comment = new Comment();
        comment.setId(rs.getInt("id"));
        comment.setContent(rs.getString("content"));
        comment.setAuthor(rs.getString("author"));
        comment.setAuthorAvatar(rs.getString("author_avatar"));
        comment.setCreateTime(rs.getTimestamp("create_time"));
        comment.setLikes(rs.getInt("likes"));
        comment.setNoteId(rs.getInt("note_id"));
        return comment;
    }
} 