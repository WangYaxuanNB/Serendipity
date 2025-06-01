package com.serendipity.demo;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import com.serendipity.demo.util.DeepSeekUtil;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChatPanel extends VBox {

    private VBox messagesArea; // Area to display messages
    private ScrollPane scrollPane; // Scroll pane for messagesArea
    private TextField inputField; // User input field
    private Button sendButton; // Send button

    // Replace with your actual API Key or load from config
    private static final String API_KEY = "sk-7519dab7e51248e2a35da17e6efea9e9";

    public ChatPanel() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.BOTTOM_CENTER); // Align elements to the bottom

        // Messages display area
        messagesArea = new VBox(5); // Spacing between messages
        messagesArea.setPadding(new Insets(5));
        messagesArea.setAlignment(Pos.TOP_LEFT); // Messages align to the top-left

        scrollPane = new ScrollPane(messagesArea);
        scrollPane.setFitToWidth(true); // Make scroll pane width fit to its content
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide horizontal scroll bar
        scrollPane.setStyle("-fx-background-color: transparent;"); // Transparent background

        // Input area
        inputField = new TextField();
        inputField.setPromptText("输入你的消息...");
        inputField.setOnAction(e -> sendMessage()); // Send on Enter key

        sendButton = new Button("发送");
        sendButton.setOnAction(e -> sendMessage());

        HBox inputArea = new HBox(5, inputField, sendButton);
        inputArea.setAlignment(Pos.CENTER);
        HBox.setHgrow(inputField, javafx.scene.layout.Priority.ALWAYS); // Input field takes available space

        this.getChildren().addAll(scrollPane, inputArea);
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS); // Scroll pane takes available vertical space

        // 设置整体背景颜色或样式 (可选)
        this.setStyle("-fx-background-color: #F0F0F0;"); // 浅灰色背景
    }

    private void sendMessage() {
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) {
            return; // Don't send empty messages
        }

        // Display user message
        addUserMessage(userInput);

        // Clear input field
        inputField.clear();

        // Get AI response in a background thread to avoid blocking UI
        new Thread(() -> {
            try {
                String aiResponse = DeepSeekUtil.chatWithDeepSeek(API_KEY, userInput);
                Platform.runLater(() -> addAIMessage(aiResponse)); // Update UI on FX application thread
            } catch (Exception e) {
                System.err.println("Error communicating with AI:");
                e.printStackTrace();
                Platform.runLater(() -> addAIMessage("错误: 无法获取AI回复")); // Show error on UI
            }
        }).start();
    }

    private void addUserMessage(String message) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true); // Allow text wrapping
        // Styling for user messages bubble
        messageLabel.setStyle("-fx-background-color: #DCF8C6; -fx-padding: 10; -fx-background-radius: 10 10 2 10;"); // Rounded corners except bottom-left

        // User Avatar
        ImageView userAvatar = new ImageView();
        try {
            Image avatarImage = new Image(getClass().getResource("/png/user_avatar.png").toExternalForm()); // 替换为你的用户头像图片路径
            userAvatar.setImage(avatarImage);
            userAvatar.setFitWidth(40);
            userAvatar.setFitHeight(40);
            userAvatar.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Error loading user avatar: " + e.getMessage());
            // Fallback to a placeholder if image fails
            Label avatarPlaceholder = new Label("User");
            avatarPlaceholder.setPrefSize(40, 40);
            avatarPlaceholder.setStyle("-fx-background-color: #C0C0C0; -fx-text-fill: white; -fx-alignment: center; -fx-background-radius: 20;");
            userAvatar = null; // Indicate using placeholder
        }

        HBox messageContainer = new HBox(5); // Spacing between avatar and message
        messageContainer.setAlignment(Pos.CENTER_RIGHT); // Align user messages to the right
        messageContainer.setPadding(new Insets(0, 5, 5, 0)); // Spacing between messages and right edge

        if (userAvatar != null) {
            messageContainer.getChildren().addAll(messageLabel, userAvatar); // Message on left, avatar on right
        } else {
            // Add placeholder if image loading failed
            Label avatarPlaceholder = new Label("User");
            avatarPlaceholder.setPrefSize(40, 40);
            avatarPlaceholder.setStyle("-fx-background-color: #C0C0C0; -fx-text-fill: white; -fx-alignment: center; -fx-background-radius: 20;");
            messageContainer.getChildren().addAll(messageLabel, avatarPlaceholder); // Message on left, placeholder on right
        }

        messagesArea.getChildren().add(messageContainer);
        scrollToBottom(); // Scroll to the latest message
    }

    private void addAIMessage(String message) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true); // Allow text wrapping
        // Styling for AI messages bubble
        messageLabel.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10; -fx-background-radius: 10 10 10 2;"); // Rounded corners except bottom-right

        // AI Avatar
        ImageView aiAvatar = new ImageView();
         try {
            Image avatarImage = new Image(getClass().getResource("/png/customer-support.png").toExternalForm()); // 替换为你的AI头像图片路径
            aiAvatar.setImage(avatarImage);
            aiAvatar.setFitWidth(40);
            aiAvatar.setFitHeight(40);
            aiAvatar.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Error loading AI avatar: " + e.getMessage());
            // Fallback to a placeholder if image fails
             Label avatarPlaceholder = new Label("AI");
            avatarPlaceholder.setPrefSize(40, 40);
            avatarPlaceholder.setStyle("-fx-background-color: #A0A0A0; -fx-text-fill: white; -fx-alignment: center; -fx-background-radius: 20;");
             aiAvatar = null; // Indicate using placeholder
        }

        HBox messageContainer = new HBox(5); // Spacing between avatar and message
        messageContainer.setAlignment(Pos.TOP_LEFT); // Align AI messages to the left and top
        messageContainer.setPadding(new Insets(0, 0, 5, 5)); // Spacing between messages and left edge

        if (aiAvatar != null) {
             messageContainer.getChildren().addAll(aiAvatar, messageLabel); // Avatar on left, message on right
        } else {
             // Add placeholder if image loading failed
             Label avatarPlaceholder = new Label("AI");
            avatarPlaceholder.setPrefSize(40, 40);
            avatarPlaceholder.setStyle("-fx-background-color: #A0A0A0; -fx-text-fill: white; -fx-alignment: center; -fx-background-radius: 20;");
             messageContainer.getChildren().addAll(avatarPlaceholder, messageLabel); // Placeholder on left, message on right
        }

        messagesArea.getChildren().add(messageContainer);
        scrollToBottom(); // Scroll to the latest message
    }

    private void scrollToBottom() {
        // Scroll to the bottom after adding a message
        scrollPane.setVvalue(1.0); // Set vertical scroll bar to maximum value (scroll to bottom)
    }
} 