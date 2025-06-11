package com.sum.view;

import javafx.scene.layout.VBox;

import java.util.Random;

/**
 * 笔记详情视图
 * 展示单个笔记的详细内容和评论
 */
public class NoteDetailView {
    private VBox noteDetailView;
    private Random random = new Random();
    
    /**
     * 构造函数
     */
    public NoteDetailView() {
        initializeUI();
    }
    
    /**
     * 初始化UI组件
     */
    private void initializeUI() {
        noteDetailView = new VBox();
        // 添加笔记详情页面的UI组件
        // TODO: 根据笔记数据填充详情页面，包括评论区
    }
    
    /**
     * 获取笔记详情视图
     * @return 详情视图的根节点
     */
    public VBox getView() {
        return noteDetailView;
    }
    
    // TODO: 添加方法来设置笔记数据和加载评论等
    
}

// 删除冗余的 Comment 类
/*
class Comment {
    private String username;
    private String avatarUrl;
    private String text;
    private String time;
    private int likes;
    
    public Comment(String username, String avatarUrl, String text, String time, int likes) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.text = text;
        this.time = time;
        this.likes = likes;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getText() { return text; }
    public String getTime() { return time; }
    public int getLikes() { return likes; }
}
*/ 