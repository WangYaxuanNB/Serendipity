package com.sum.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;

public class DeepSeekUtil {
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final  OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30000, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30000, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30000, java.util.concurrent.TimeUnit.SECONDS)
            .build();
    private static final Gson gson = new Gson();

    /**
     * 调用 DeepSeek API 进行对话
     * @param apiKey 你的API Key
     * @param userInput 用户输入内容
     * @return AI 回复内容
     * @throws Exception 网络或解析异常
     */
    public static String chatWithDeepSeek(String apiKey, String userInput) throws Exception {
        // 构建请求体
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", userInput);

        JsonArray messages = new JsonArray();
        messages.add(message);

        JsonObject data = new JsonObject();
        data.addProperty("model", "deepseek-chat");
        data.add("messages", messages);

        RequestBody body = RequestBody.create(
                gson.toJson(data),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // 解析返回内容
                String responseBody = response.body().string();
                JsonObject json = gson.fromJson(responseBody, JsonObject.class);
                // 解析 AI 回复内容
                JsonArray choices = json.getAsJsonArray("choices");
                if (choices != null && choices.size() > 0) {
                    JsonObject firstChoice = choices.get(0).getAsJsonObject();
                    JsonObject messageObj = firstChoice.getAsJsonObject("message");
                    if (messageObj != null && messageObj.has("content")) {
                        return messageObj.get("content").getAsString();
                    }
                }
                return "未获取到AI回复内容";
            } else {
                return "请求失败，状态码：" + response.code();
            }
        }
    }
} 