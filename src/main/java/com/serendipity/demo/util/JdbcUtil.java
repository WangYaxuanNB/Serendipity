package com.serendipity.demo.util;

import java.sql.*;

/**
 * JDBC数据库连接工具类
 */
public class JdbcUtil {

    // 数据库连接参数
    private static final String URL = "jdbc:mysql://localhost:3306/fx_dateing?useSSL=false&serverTimezone=UTC&characterEncoding=utf8&useUnicode=true";
    private static final String USER = "root";
    private static final String PASSWORD = "dhc040617";

    // 加载数据库驱动
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("无法加载数据库驱动", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接对象
     * @throws SQLException 如果连接失败
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 关闭数据库连接
     *
     * @param connection 要关闭的连接对象
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭 Statement 或 PreparedStatement
     *
     * @param statement 要关闭的 Statement 对象
     */
    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭 ResultSet
     *
     * @param resultSet 要关闭的 ResultSet 对象
     */
    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置 PreparedStatement 的参数
     *
     * @param stmt   PreparedStatement 对象
     * @param params 参数数组
     * @throws SQLException 如果设置参数失败
     */
    public static void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
        }
    }

    /**
     * 执行更新操作 (INSERT, UPDATE, DELETE)
     *
     * @param sql    SQL更新语句
     * @param params 参数数组
     * @return 受影响的行数
     * @throws SQLException 如果执行失败
     */
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, params);
            return stmt.executeUpdate();
        } finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    /**
     * 执行查询操作
     * 注意：此方法返回 ResultSet，调用者需要负责关闭 ResultSet 及其底层的 Connection 和 Statement。
     * 推荐使用 try-with-resources 来确保资源正确关闭。
     *
     * @param sql    SQL查询语句
     * @param params 参数数组
     * @return 查询结果集 ResultSet
     * @throws SQLException 如果执行失败
     */
    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, params);
            rs = stmt.executeQuery();
            // 返回 ResultSet，caller 负责关闭 conn, stmt, rs
            return rs;
        } catch (SQLException e) {
            // 出现异常时确保资源关闭
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
            throw e; // 重新抛出异常
        }
    }

    // 可以根据需要添加更多便利的查询方法，例如返回 List<Map<String, Object>> 等
} 