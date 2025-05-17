package com.sum;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * 消息通知视图
 */
public class UniqueMessageView {
    private VBox messageList;
    
    public UniqueMessageView() {
        // 初始化消息列表布局
        messageList = new VBox(10);
        messageList.getStyleClass().add("message-list");
        
        // 创建消息标签
        Label messageLabel = new Label("通知消息");
        messageLabel.getStyleClass().add("section-title");
        
        // 使用 javafx.geometry.Insets 设置内边距
        messageLabel.setPadding(new Insets(20, 0, 0, 0)); // 增加顶部内边距
        
        // 创建消息列表
        ListView<String> listView = new ListView<>();
        
        // 添加示例消息数据
        List<String> messages = getSampleMessages();
        ObservableList<String> observableMessages = FXCollections.observableArrayList(messages);
        listView.setItems(observableMessages);
        
        // 将元素添加到布局中
        messageList.getChildren().addAll(messageLabel, listView);
    }

    // 获取示例消息数据的方法
    private List<String> getSampleMessages() {
        return List.of("消息1", "消息2", "消息3");
    }
}