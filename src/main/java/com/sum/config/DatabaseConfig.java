package com.sum.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class DatabaseConfig {
    private static final String URL = "jdbc:sqlite:notes.db";
    
    private static Connection connection;
    
    static {
        try {
            // 初始化数据库
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 读取schema.sql文件
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            DatabaseConfig.class.getResourceAsStream("/schema.sql")))) {
                
                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sql.append(line);
                    if (line.trim().endsWith(";")) {
                        stmt.execute(sql.toString());
                        sql.setLength(0);
                    }
                }
            } catch (IOException e) {
                throw new SQLException("Error reading schema.sql", e);
            }
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC Driver not found.", e);
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 