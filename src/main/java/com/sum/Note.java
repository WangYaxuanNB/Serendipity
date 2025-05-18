package com.sum;

/**
 * 笔记数据模型类
 */
public class Note {
    private String title;          // 笔记标题
    private String description;    // 笔记描述
    private String author;         // 作者名
    private String imageUrl;       // 图片URL
    private String authorAvatarUrl; // 作者头像URL
    private int likes;             // 点赞数
    private int comments;          // 评论数
    private int imageHeight;       // 图片高度
    
    public Note(String title, String description, String author, String imageUrl, 
                String authorAvatarUrl, int likes, int comments, int imageHeight) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.imageUrl = imageUrl;
        this.authorAvatarUrl = authorAvatarUrl;
        this.likes = likes;
        this.comments = comments;
        this.imageHeight = imageHeight;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
    public String getImageUrl() { return imageUrl; }
    public String getAuthorAvatarUrl() { return authorAvatarUrl; }
    public int getLikes() { return likes; }
    public int getComments() { return comments; }
    public int getImageHeight() { return imageHeight; }
}
