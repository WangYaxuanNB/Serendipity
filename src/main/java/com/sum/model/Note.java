package com.sum.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

public class Note {
    private int id;
    private String title;
    private String description;
    private String author;
    private String imageUrl;
    private String authorAvatarUrl;
    private int likes;
    private int commentCount;
    private int imageHeight;
    private Timestamp createdAt;
    private List<Comment> comments;

    public Note() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.likes = 0;
        this.commentCount = 0;
        this.comments = new ArrayList<>();
    }

    // Getter 方法
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
    public String getImageUrl() { return imageUrl; }
    public String getAuthorAvatarUrl() { return authorAvatarUrl; }
    public int getLikes() { return likes; }
    public int getCommentCount() { return commentCount; }
    public int getImageHeight() { return imageHeight; }
    public List<Comment> getComments() { return comments; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Setter 方法
    public void setId(int id) { this.id = id; }
    public void setLikes(int likes) { this.likes = likes; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
    public void setImageHeight(int imageHeight) { this.imageHeight = imageHeight; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setAuthor(String author) { this.author = author; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setAuthorAvatarUrl(String authorAvatarUrl) { this.authorAvatarUrl = authorAvatarUrl; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public void addComment(Comment comment) {
        if (comment != null) {
            comments.add(comment);
            // 不在这里增加commentCount，因为数据库中的计数应该由数据库操作来维护
        }
    }
} 