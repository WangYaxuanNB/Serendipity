package com.sum.dao;

import com.sum.model.Note;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class community_interactions {

    // 假设已有数据库连接方法
    private Connection getConnection() {
        // 实现数据库连接逻辑
        return null;
    }

    // 新增方法：更新首页推荐内容
    public void updateHomePageRecommendation(int postId) {
        String sql = "INSERT INTO home_recommendation (post_id, created_at) VALUES (?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.executeUpdate();
            System.out.println("帖子已成功添加到首页推荐列表！");
        } catch (SQLException e) {
            System.err.println("更新首页推荐失败：" + e.getMessage());
        }
    }

    // 假设已有其他方法
    public void publishPost(String title, String content) {
        String sql = "INSERT INTO posts (title, content, created_at) VALUES (?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.executeUpdate();

            // 获取新插入的帖子ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int postId = rs.getInt(1);
                    System.out.println("帖子发布成功，ID为：" + postId);

                    // 调用更新首页推荐的方法
                    updateHomePageRecommendation(postId);
                }
            }
        } catch (SQLException e) {
            System.err.println("发布帖子失败：" + e.getMessage());
        }
    }

    // 新增方法：查询所有互动（模拟数据）
    public static List<Note> queryAllInteractions() {
        List<Note> notes = new ArrayList<>();
        // 这里可以添加从数据库获取数据的逻辑
        // 为了演示，我们添加一些模拟数据
        Note note1 = new Note();
        note1.setTitle("模拟笔记1");
        note1.setDescription("这是一篇模拟笔记的内容");
        note1.setAuthor("作者1");
        note1.setImageUrl("https://picsum.photos/seed/1/400/300");
        note1.setAuthorAvatarUrl("https://randomuser.me/api/portraits/women/1.jpg");
        note1.setImageHeight(300);
        note1.setLikes(10);
        note1.setCommentCount(5);
        notes.add(note1);

        Note note2 = new Note();
        note2.setTitle("模拟笔记2");
        note2.setDescription("这是另一篇模拟笔记的内容");
        note2.setAuthor("作者2");
        note2.setImageUrl("https://picsum.photos/seed/2/400/300");
        note2.setAuthorAvatarUrl("https://randomuser.me/api/portraits/men/2.jpg");
        note2.setImageHeight(300);
        note2.setLikes(15);
        note2.setCommentCount(3);
        notes.add(note2);

        return notes;
    }
}