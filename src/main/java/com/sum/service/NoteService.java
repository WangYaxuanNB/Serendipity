package com.sum.service;

import com.sum.dao.NoteDao;
import com.sum.dao.impl.NoteDaoImpl;
import com.sum.model.Note;
import java.sql.SQLException;
import java.util.List;

public class NoteService {
    private final NoteDao noteDao;

    public NoteService() {
        this.noteDao = new NoteDaoImpl();
    }

    public Note createNote(String title, String description, String author, String imageUrl, 
                         String authorAvatarUrl, int imageHeight) throws SQLException {
        Note note = new Note();
        note.setTitle(title);
        note.setDescription(description);
        note.setAuthor(author);
        note.setImageUrl(imageUrl);
        note.setAuthorAvatarUrl(authorAvatarUrl);
        note.setImageHeight(imageHeight);
        return noteDao.create(note);
    }

    public List<Note> getAllNotes() throws SQLException {
        return noteDao.getAll();
    }

    public List<Note> getHotNotes(int limit) throws SQLException {
        return noteDao.getHotNotes(limit);
    }

    public List<Note> getNotesByAuthor(String author) throws SQLException {
        return noteDao.getByAuthor(author);
    }

    public void incrementLikes(int noteId) throws SQLException {
        noteDao.incrementLikes(noteId);
    }

    public void incrementCommentCount(int noteId) throws SQLException {
        noteDao.incrementCommentCount(noteId);
    }

    public void decrementCommentCount(int noteId) throws SQLException {
        Note note = noteDao.getById(noteId);
        if (note != null) {
            note.setCommentCount(note.getCommentCount() - 1);
            noteDao.update(note);
        }
    }

    public int getCommentCount(int noteId) throws SQLException {
        Note note = noteDao.getById(noteId);
        return note != null ? note.getCommentCount() : 0;
    }
} 