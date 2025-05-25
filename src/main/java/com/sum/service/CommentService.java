package com.sum.service;

import com.sum.dao.CommentDao;
import com.sum.dao.impl.CommentDaoImpl;
import com.sum.model.Comment;
import java.sql.SQLException;
import java.util.List;

public class CommentService {
    private final CommentDao commentDao;
    private final NoteService noteService;

    public CommentService() {
        this.commentDao = new CommentDaoImpl();
        this.noteService = new NoteService();
    }

    public void createComment(Comment comment) throws SQLException {
        commentDao.create(comment);
    }

    public List<Comment> getCommentsByNoteId(int noteId) throws SQLException {
        return commentDao.getByNoteId(noteId);
    }

    public List<Comment> getCommentsByAuthor(String author) throws SQLException {
        return commentDao.getByAuthor(author);
    }

    public void incrementLikes(int commentId) throws SQLException {
        commentDao.incrementLikes(commentId);
    }

    public void incrementNoteCommentCount(int noteId) throws SQLException {
        noteService.incrementCommentCount(noteId);
    }

    public int getCommentCount(int noteId) throws SQLException {
        return commentDao.getCommentCount(noteId);
    }

    public void deleteComment(int commentId, int noteId) throws SQLException {
        commentDao.delete(commentId);
        noteService.decrementCommentCount(noteId);
    }
} 