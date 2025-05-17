package com.sum;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MessageView {
    private VBox messageList;

    public MessageView() {
        // 初始化消息列表布局
        messageList = new VBox(10);
        messageList.getStyleClass().add("message-list");

        // 创建消息标签
        Label messageLabel = new Label("通知消息");
        messageLabel.getStyleClass().add("section-title");

        // 设置内边距（注意这是 JavaFX 的 Insets）
        messageLabel.setPadding(new Insets(10, 0, 0, 6));

        // 创建消息列表
        ListView<String> listView = new ListView<>();

        // 添加示例消息数据
        List<String> messages = getSampleMessages();
        listView.getItems().addAll(messages);

        // 添加到布局
        messageList.getChildren().addAll(messageLabel, listView);
    }

    public VBox getMessageList() {
        return messageList;
    }

    private List<String> getSampleMessages() {
        List<String> messages = new ArrayList<>();
        messages.add("欢迎来到Serendipity！");
        messages.add("你有一条新的关注请求");
        messages.add("系统更新将于今晚12点进行");
        return messages;
    }
}