package com.sum.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class 查询community_interactions {
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

    public static void main(String[] args) {
        Connection conn = null;
        ResultSet rs=null;
        try {
            conn = getConnection();
            System.out.println("数据库连接成功！");
            rs=conn.createStatement().executeQuery("SELECT * FROM community_interactions");
            while (rs.next()) {
                System.out.println(rs.getString("title") + " " + rs.getString("content"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(conn);
        }
    }
}