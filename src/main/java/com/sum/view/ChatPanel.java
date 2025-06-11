package com.sum.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import com.sum.utils.DeepSeekUtil;

/**
 * 人工客服面板
 */
public class ChatPanel extends VBox {
    private VBox messagesArea; // 消息显示区域
    private ScrollPane scrollPane; // 消息区域滚动面板
    private TextField inputField; // 用户输入框
    private Button sendButton; // 发送按钮
    
    // DeepSeek API密钥
    private static final String API_KEY = "sk-7519dab7e51248e2a35da17e6efea9e9";

    public ChatPanel() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setStyle("-fx-background-color: #F0F0F0;"); // 浅灰色背景
        this.setAlignment(Pos.CENTER); // 居中对齐

        // 创建消息显示区域
        messagesArea = new VBox(10); // 消息间距10
        messagesArea.setPadding(new Insets(10));
        messagesArea.setAlignment(Pos.TOP_LEFT); // 靠左上对齐

        // 创建滚动面板包含消息区域
        scrollPane = new ScrollPane(messagesArea);
        scrollPane.setFitToWidth(true); // 宽度适应内容
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // 需要时显示垂直滚动条
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // 不显示水平滚动条
        scrollPane.setStyle("-fx-background-color: transparent;"); // 透明背景

        // 输入区域
        inputField = new TextField();
        inputField.setPromptText("输入你的消息...");
        inputField.setPrefHeight(35);
        inputField.setOnAction(e -> sendMessage()); // 按回车发送消息

        sendButton = new Button("发送");
        sendButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        sendButton.setPrefHeight(35);
        sendButton.setOnAction(e -> sendMessage());

        // 输入区域布局
        HBox inputArea = new HBox(5, inputField, sendButton);
        inputArea.setAlignment(Pos.CENTER);
        inputArea.setPadding(new Insets(5, 0, 0, 0));
        HBox.setHgrow(inputField, Priority.ALWAYS); // 输入框占据所有可用空间

        // 添加组件到面板
        this.getChildren().addAll(scrollPane, inputArea);
        VBox.setVgrow(scrollPane, Priority.ALWAYS); // 滚动面板占据所有可用垂直空间

        // 显示初始欢迎消息
        Platform.runLater(() -> {
            addServiceMessage("您好！我是您的智能心情助手。请问有什么可以帮助您的？");
            addServiceMessage("您可以咨询关于心情记录、心情分析、心理健康或任何困扰您的问题，我会尽力为您解答。");
        });
    }

    private void sendMessage() {
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) {
            return; // 不发送空消息
        }

        // 显示用户消息
        addUserMessage(userInput);

        // 清空输入框
        inputField.clear();

        // 根据用户输入生成回复
        generateResponse(userInput);
    }

    private void addUserMessage(String message) {
        // 创建用户消息气泡
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true); // 允许文字换行
        messageLabel.setMaxWidth(300); // 限制最大宽度
        // 用户消息气泡样式
        messageLabel.setStyle("-fx-background-color: #DCF8C6; " + 
                              "-fx-padding: 10; " + 
                              "-fx-background-radius: 10 10 2 10;"); // 圆角气泡，左下角除外

        // 用户头像
        StackPane userAvatar = new StackPane();
        userAvatar.setPrefSize(40, 40);
        userAvatar.setStyle("-fx-background-color: #4CAF50; " + // 绿色背景
                            "-fx-background-radius: 20;"); // 圆形头像
        
        Label avatarLabel = new Label("我");
        avatarLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        userAvatar.getChildren().add(avatarLabel);

        // 消息容器
        HBox messageContainer = new HBox(5); // 头像和消息间距5
        messageContainer.setAlignment(Pos.CENTER_RIGHT); // 用户消息靠右
        messageContainer.setPadding(new Insets(5));
        messageContainer.getChildren().addAll(messageLabel, userAvatar); // 消息在左，头像在右

        messagesArea.getChildren().add(messageContainer);
        scrollToBottom(); // 滚动到底部
    }

    private void addServiceMessage(String message) {
        // 创建客服消息气泡
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true); // 允许文字换行
        messageLabel.setMaxWidth(300); // 限制最大宽度
        // 客服消息气泡样式
        messageLabel.setStyle("-fx-background-color: #E0E0E0; " + 
                              "-fx-padding: 10; " + 
                              "-fx-background-radius: 10 10 10 2;"); // 圆角气泡，右下角除外

        // 客服头像
        StackPane serviceAvatar = new StackPane();
        serviceAvatar.setPrefSize(40, 40);
        serviceAvatar.setStyle("-fx-background-color: #007bff; " + // 蓝色背景
                               "-fx-background-radius: 20;"); // 圆形头像
        
        Label avatarLabel = new Label("客");
        avatarLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        serviceAvatar.getChildren().add(avatarLabel);

        // 消息容器
        HBox messageContainer = new HBox(5); // 头像和消息间距5
        messageContainer.setAlignment(Pos.CENTER_LEFT); // 客服消息靠左
        messageContainer.setPadding(new Insets(5));
        messageContainer.getChildren().addAll(serviceAvatar, messageLabel); // 头像在左，消息在右

        messagesArea.getChildren().add(messageContainer);
        scrollToBottom(); // 滚动到底部
    }

    private void scrollToBottom() {
        // 滚动到底部
        scrollPane.applyCss();
        scrollPane.layout();
        scrollPane.setVvalue(1.0); // 设置垂直滚动条到最大值（底部）
    }

    private void generateResponse(String userInput) {
        // 模拟客服思考，实际是在等待API响应
        new Thread(() -> {
            try {
                // 显示正在输入的提示
                Platform.runLater(() -> addServiceMessage("正在思考..."));
                
                // 调用DeepSeek API获取回复
                String aiResponse = DeepSeekUtil.chatWithDeepSeek(API_KEY, userInput);
                
                // 移除"正在思考"消息
                Platform.runLater(() -> {
                    // 移除最后一条消息（正在思考）
                    messagesArea.getChildren().remove(messagesArea.getChildren().size() - 1);
                    // 添加AI回复
                    addServiceMessage(aiResponse);
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    // 移除最后一条消息（正在思考）
                    if (!messagesArea.getChildren().isEmpty()) {
                        messagesArea.getChildren().remove(messagesArea.getChildren().size() - 1);
                    }
                    // 显示错误消息
                    addServiceMessage("抱歉，我暂时无法回答您的问题，请稍后再试。");
                });
            }
        }).start();
    }

    public VBox getRoot() {
        return this;
    }
} 