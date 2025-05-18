package com.sum.dao;

import com.sum.Note;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class community_interactions {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/serendipity";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //     创建数据库连接
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
    //    关闭数据库连接
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//   插入数据
    public static void createInteraction(String title, String content) {
//        1、定义插入的sql语句
        String sql = "INSERT INTO community_interactions (title, content) VALUES (?, ?)";
//        2、初始化JDBC返回内容
        Connection conn = null;
        ResultSet rs=null;
        PreparedStatement pstmet=null;
//        3、执行SQL语句、执行插入
        try {
//
            conn = getConnection();
            System.out.println("数据库连接成功！");
            pstmet=conn.prepareStatement(sql);
            pstmet.setString(1, title);
            pstmet.setString(2, content);
//          执行语句
            int rows=pstmet.executeUpdate();
            if(rows>0){
                System.out.println("插入成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    查询所有笔记，返回一个列表
    public static List queryAllInteractions() {
        String sql = "SELECT * FROM community_interactions";
        Connection conn = null;
        ResultSet rs=null;
        Statement pstmet=null;
//       集合
        List interactions = new ArrayList<>();
        try {
            conn = getConnection();
            System.out.println("数据库连接成功！");
            pstmet=conn.createStatement();
            rs=pstmet.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("interaction_id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                interactions.add(new Note(title, content, null, null, null, 0, 0, 0));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return interactions;
    }

    public static void main(String[] args) {
//       插入示例
//        createInteraction("这是一条新帖子","这是新帖子的内容");
//       查询所有笔记
        List<Note> interactions = queryAllInteractions();
        for (Note interaction : interactions) {
            System.out.println("标题：" + interaction.getTitle());
            System.out.println("内容：" + interaction.getDescription());
            System.out.println("-------------------------");
        }
    }
}