package com.serendipity.demo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * 简易 HTTP POST 工具类
 */
public class HttpClient {

    public static String sendPost(String url, String json) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = null;

        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // 写入 JSON 请求体
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes("UTF-8"));
            }

            // 获取响应码
            int status = conn.getResponseCode();
            InputStream is;

            // 判断用哪种流读取：成功流 or 错误流
            if (status >= 200 && status < 300) {
                is = conn.getInputStream();
            } else {
                is = conn.getErrorStream();
            }

            // 读取响应体
            try (Scanner in = new Scanner(is, "UTF-8")) {
                while (in.hasNextLine()) {
                    response.append(in.nextLine());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.append("请求异常：").append(e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return response.toString();
    }
}
