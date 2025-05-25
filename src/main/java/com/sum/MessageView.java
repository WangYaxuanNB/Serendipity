package com.sum;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MessageView {
    private VBox messageList;

    public MessageView() {
        initializeUI();
    }

    private void initializeUI() {
        // 初始化消息列表布局
        messageList = new VBox(10);
        messageList.getStyleClass().add("message-list");
        
        // 创建消息标签
        Label messageLabel = new Label("通知消息");
        messageLabel.getStyleClass().add("section-title");
        
        // 调整顶部内边距，使其与下方灰色区域对齐
        messageLabel.setPadding(new Insets(5, 0, 0, 0));
        
        // 创建消息列表
        ListView<String> listView = new ListView<>();
        
        // 添加示例消息数据
        List<String> messages = getSampleMessages();
        ObservableList<String> observableMessages = FXCollections.observableArrayList(messages);
        listView.setItems(observableMessages);
        
        // 将元素添加到布局中
        messageList.getChildren().addAll(messageLabel, listView);
    }

    /**
     * 获取消息视图的根节点
     * @return 消息视图
     */
    public VBox getView() {
        return messageList;
    }

    // 获取示例消息数据的方法
    private List<String> getSampleMessages() {
        List<String> messages = new ArrayList<>();
        messages.add("欢迎来到Serendipity！");
        messages.add("你有一条新的关注请求");
        messages.add("系统更新将于今晚12点进行");
        return messages;
    }
}