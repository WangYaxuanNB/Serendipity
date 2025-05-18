package com.serendipity.demo.util;

import javax.servlet.http.HttpServletRequest;

/**
 * JwtToken 工具类（模拟版）
 */
public class JwtTokenUtil {

    // Token 在请求头中的字段名
    private static final String TOKEN_NAME = "Authorization";

    /**
     * 获取 token 名称（用于设置响应头）
     */
    public static String getTokenName() {
        return TOKEN_NAME;
    }

    /**
     * 生成 token（模拟用，后续可换成真实 JWT）
     */
    public static String generateToken(String username) {
        return "token-" + username + "-" + System.currentTimeMillis();
    }

    /**
     * 从请求中获取 token（从 header 或 param）
     */
    public static String getToken(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request 不能为空");
        }

        String token = request.getHeader(TOKEN_NAME);
        if (token == null || token.trim().isEmpty()) {
            token = request.getParameter(TOKEN_NAME);
        }
        return token;
    }
}
