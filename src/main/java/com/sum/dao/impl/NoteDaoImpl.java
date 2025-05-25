package com.sum.dao.impl;

import com.sum.config.DatabaseConfig;
import com.sum.dao.NoteDao;
import com.sum.model.Note;
import com.sum.model.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public  class NoteDaoImpl implements NoteDao {
    
    @Override
    public Note create(Note note) {
        String sql = "INSERT INTO notes (title, description, author, image_url, author_avatar_url, likes, comment_count, image_height, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, note.getTitle());
            pstmt.setString(2, note.getDescription());
            pstmt.setString(3, note.getAuthor());
            pstmt.setString(4, note.getImageUrl());
            pstmt.setString(5, note.getAuthorAvatarUrl());
            pstmt.setInt(6, note.getLikes());
            pstmt.setInt(7, note.getCommentCount());
            pstmt.setInt(8, note.getImageHeight());
            pstmt.setTimestamp(9, note.getCreatedAt());
            
            pstmt.executeUpdate();
            
            // 获取生成的ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    note.setId(rs.getInt(1));
                }
            }
            
            return note;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Note getById(int id) {
        String sql = "SELECT * FROM notes WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractNoteFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Note> getAll() {
        String sql = "SELECT * FROM notes ORDER BY id DESC";
        List<Note> notes = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                notes.add(extractNoteFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }
    
    @Override
    public List<Note> getByAuthor(String author) {
        String sql = "SELECT * FROM notes WHERE author = ? ORDER BY id DESC";
        List<Note> notes = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, author);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                notes.add(extractNoteFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }
    
    @Override
    public List<Note> getHotNotes(int limit) {
        String sql = "SELECT * FROM notes ORDER BY likes DESC LIMIT ?";
        List<Note> notes = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                notes.add(extractNoteFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }
    
    @Override
    public void update(Note note) {
        String sql = "UPDATE notes SET title = ?, description = ?, author = ?, image_url = ?, author_avatar_url = ?, likes = ?, comment_count = ?, image_height = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, note.getTitle());
            pstmt.setString(2, note.getDescription());
            pstmt.setString(3, note.getAuthor());
            pstmt.setString(4, note.getImageUrl());
            pstmt.setString(5, note.getAuthorAvatarUrl());
            pstmt.setInt(6, note.getLikes());
            pstmt.setInt(7, note.getCommentCount());
            pstmt.setInt(8, note.getImageHeight());
            pstmt.setInt(9, note.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM notes WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void incrementLikes(int id) {
        String sql = "UPDATE notes SET likes = likes + 1 WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void incrementCommentCount(int id) {
        String sql = "UPDATE notes SET comment_count = comment_count + 1 WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private Note extractNoteFromResultSet(ResultSet rs) throws SQLException {
        Note note = new Note();
        note.setId(rs.getInt("id"));
        note.setTitle(rs.getString("title"));
        note.setDescription(rs.getString("description"));
        note.setAuthor(rs.getString("author"));
        note.setImageUrl(rs.getString("image_url"));
        note.setAuthorAvatarUrl(rs.getString("author_avatar_url"));
        note.setLikes(rs.getInt("likes"));
        note.setCommentCount(rs.getInt("comment_count"));
        note.setImageHeight(rs.getInt("image_height"));
        note.setCreatedAt(rs.getTimestamp("created_at"));
        // 初始化comments列表
        note.getComments().clear(); // 确保列表为空
        return note;
    }
}


