package com.serendipity.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import com.serendipity.demo.util.JdbcUtil; // 导入 JdbcUtil
import javafx.stage.Stage; // Import Stage
import javafx.scene.layout.Priority; // Import Priority
import javafx.scene.layout.StackPane; // Import StackPane
import javafx.animation.ScaleTransition; // Import ScaleTransition

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp; // 导入 Timestamp
import java.time.LocalDateTime; // 导入 LocalDateTime
import java.util.Arrays;
import java.util.List;

public class MoodSelectPage {

    private MainApp mainApp;
    private Scene scene;
    private VBox root;
    private VBox moodCardDisplayArea; // 用于显示当前心情卡片的区域
    private int currentMoodIndex = 0; // 当前显示的心情索引
    private double dragStartX;
    private static final double DRAG_THRESHOLD = 100.0; // 拖动阈值

    // 预设心情数据 (英文名称，中文名称，图片名，背景颜色/样式 - 简化处理)
    private static final List<String[]> MOODS = Arrays.asList(
            new String[]{"Angry", "生气", "angry.png", "-fx-background-color: #FF7043;"}, // 示例颜色
            new String[]{"Upset", "烦躁", "upset.png", "-fx-background-color: #7986CB;"}, // 示例颜色
            new String[]{"Sad", "伤心", "sad.png", "-fx-background-color: #A1E7EB;"},
            new String[]{"Good", "开心", "good.png", "-fx-background-color: #FFCA28;"}, // 示例颜色
            new String[]{"Happy", "兴奋", "happy.png", "-fx-background-color:#DFEBFF;"},// 示例颜色
            new String[]{"Spectacular", "心动", "spectacular.png", "-fx-background-color:rgb(255, 171, 199);"} // 示例颜色
    );

    public MoodSelectPage(MainApp mainApp) {
        this.mainApp = mainApp;

        root = new VBox(20); // 主布局容器，垂直排列
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #FDF5F5;"); // 背景色示例

        // Top area for greeting and back element
        HBox topArea = new HBox(20); // Spacing between elements
        topArea.setAlignment(Pos.CENTER_LEFT); // Align elements to the left

        // 返回元素 (使用 ImageView 和动画或备用文本)
        StackPane backElementPane;
        ImageView backArrowIcon = new ImageView();
        try {
            // Use a placeholder image path for the back arrow
            Image backArrowImage = new Image(getClass().getResourceAsStream("/icons/back_arrow.png")); // Replace with actual path if needed
             if (backArrowImage.isError()) {
                 throw new Exception("Image loading failed");
            }
            backArrowIcon.setImage(backArrowImage);
            backArrowIcon.setFitWidth(30); // Adjust size as needed
            backArrowIcon.setFitHeight(30);
            backElementPane = new StackPane(backArrowIcon);

        } catch (Exception e) {
            System.err.println("Failed to load back arrow icon: " + e.getMessage());
            // Fallback: Use a simple text label if image fails
            Label fallbackBackLabel = new Label("← 返回");
            fallbackBackLabel.setFont(Font.font("Arial", 16));
            fallbackBackLabel.setStyle("-fx-text-fill: #333333;");
            backElementPane = new StackPane(fallbackBackLabel);
        }

        backElementPane.setStyle("-fx-cursor: hand;"); // Change cursor to hand on hover

        // Add hover animation to the back element pane
        setupBackButtonHoverAnimation(backElementPane);

        // Add click handler to the back element pane
        setupBackButtonClickHandler(backElementPane);

        // 顶部的问候语
        Label greetingLabel = new Label("How are you feeling today?");
        greetingLabel.setFont(Font.font("Arial", 26));
        HBox.setHgrow(greetingLabel, Priority.ALWAYS); // Allow greeting to take available space
        greetingLabel.setAlignment(Pos.CENTER); // Center the greeting in the remaining space

        topArea.getChildren().addAll(backElementPane, greetingLabel);

        // 心情卡片显示区域
        moodCardDisplayArea = new VBox(); // 使用VBox作为卡片容器
        moodCardDisplayArea.setAlignment(Pos.CENTER);

        // "Select mood" 按钮
        Button selectMoodButton = new Button("Select mood");
        selectMoodButton.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 25; -fx-padding: 10 40;"); // 按钮样式示例
        selectMoodButton.setOnAction(e -> {
            String selectedMood = MOODS.get(currentMoodIndex)[1]; // 获取当前选中心情的中文名称
            System.out.println("选择的心情是: " + selectedMood);
            
            // 记录选择的心情到数据库
            saveMoodRecord(selectedMood);

            // 跳转到日历页面
            try {
                 mainApp.showCalendarViewPage(); // 在这里调用跳转方法
            } catch (java.io.IOException ex) { // 明确捕获 IOException
                System.out.println("跳转到日历页面失败: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // 人工客服按钮
        Button chatButton = new Button("人工客服");
        chatButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 20; -fx-padding: 5 15;");
        chatButton.setOnAction(e -> showChatPanel());

        root.getChildren().addAll(topArea, moodCardDisplayArea, selectMoodButton, chatButton); // Add topArea to layout

        this.scene = new Scene(root, 400, 600); // 场景大小示例

        // 初始化显示第一个心情卡片
        updateMoodCardDisplay();
    }

    public Scene getScene() {
        return scene;
    }

    // 根据当前索引更新显示的心情卡片
    private void updateMoodCardDisplay() {
        moodCardDisplayArea.getChildren().clear(); // 清除旧的卡片
        moodCardDisplayArea.getChildren().add(createMoodCard(currentMoodIndex)); // 添加新的卡片
    }

    // 创建单个心情卡片的Node
    private VBox createMoodCard(int index) {
        String[] moodData = MOODS.get(index);
        String englishName = moodData[0];
        String chineseName = moodData[1];
        String imageName = moodData[2];
        String bgColor = moodData[3];

        VBox card = new VBox(10); // 卡片容器
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-radius: 15; " + bgColor); // 圆角和背景色
        card.setPrefSize(250, 300); // 卡片大小示例

        // 添加鼠标事件处理
        card.setOnMousePressed(e -> {
            dragStartX = e.getSceneX();
        });

        card.setOnMouseDragged(e -> {
            double dragDelta = e.getSceneX() - dragStartX;
            card.setTranslateX(dragDelta);
        });

        card.setOnMouseReleased(e -> {
            double dragDelta = e.getSceneX() - dragStartX;
            if (Math.abs(dragDelta) > DRAG_THRESHOLD) {
                if (dragDelta > 0) {
                    // 向右拖动，显示上一个心情
                    currentMoodIndex = (currentMoodIndex - 1 + MOODS.size()) % MOODS.size();
                } else {
                    // 向左拖动，显示下一个心情
                    currentMoodIndex = (currentMoodIndex + 1) % MOODS.size();
                }
                updateMoodCardDisplay();
            } else {
                // 如果拖动距离不够，回弹到原位
                TranslateTransition tt = new TranslateTransition(Duration.millis(200), card);
                tt.setToX(0);
                tt.play();
            }
        });

        // 卡片内的问候语
        Label cardGreetingLabel = new Label("How are you feeling today?");
        cardGreetingLabel.setFont(Font.font("Arial", 16));
        cardGreetingLabel.setStyle("-fx-text-fill: white;"); // 文字颜色示例

        // 心情名称
        Label moodNameLabel = new Label(englishName); // 显示英文名称，如果需要显示中文可以修改
        moodNameLabel.setFont(Font.font("Arial", 24));
        moodNameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;"); // 文字颜色示例

        // 心情图标
        ImageView moodIcon = new ImageView();
        try {
            // TODO: 使用实际的心情图标路径
            Image img = new Image(MoodSelectPage.class.getResource("/mood/" + imageName).toExternalForm());
            moodIcon.setImage(img);
        } catch (Exception e) {
            System.out.println("图标加载失败: " + imageName);
            // 如果图片加载失败，可以使用占位符或者显示错误信息
        }
        moodIcon.setFitWidth(80); // 图标大小示例
        moodIcon.setFitHeight(80);

        // 下方的小圆点指示器 (简化处理，只创建)
        HBox indicators = new HBox(5); // 圆点间距
        indicators.setAlignment(Pos.CENTER);
        for (int i = 0; i < MOODS.size(); i++) {
            Region indicator = new Region();
            indicator.setPrefSize(8, 8);
            indicator.setStyle("-fx-background-color: " + (i == currentMoodIndex ? "white" : "#ccc") + "; -fx-background-radius: 4;"); // 当前圆点白色，其他灰色
            indicators.getChildren().add(indicator);
        }

        card.getChildren().addAll(cardGreetingLabel, moodNameLabel, moodIcon, indicators);

        return card;
    }

    // 将心情记录保存到数据库
    private void saveMoodRecord(String mood) {
        // 获取当前心情的完整数据
        String[] currentMoodData = MOODS.get(currentMoodIndex);
        String moodText = currentMoodData[1];  // 中文名称
        String moodImage = currentMoodData[2]; // 图片名称
        
        // 假设 moods 表有 id (自增), user_id (关联用户), mood (心情文本), fh (图片地址), record_time (记录时间)字段
        long userId = mainApp.getCurrentUserId(); // 获取当前登录用户ID
        String sql = "INSERT INTO moods (user_id, mood, fh, record_time) VALUES (?, ?, ?, ?)";

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            JdbcUtil.setParameters(stmt, userId, moodText, moodImage, Timestamp.valueOf(LocalDateTime.now()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("心情记录保存成功: " + moodText + ", 图片: " + moodImage);
            } else {
                System.out.println("心情记录保存失败: " + moodText);
            }
        } catch (SQLException e) {
            System.err.println("保存心情记录到数据库失败:");
            e.printStackTrace();
            // TODO: 显示错误信息给用户
        }
    }

    // Method to show the chat panel
    private void showChatPanel() {
        try {
            Stage chatStage = new Stage();
            chatStage.setTitle("人工客服");
            ChatPanel chatPanel = new ChatPanel();
            Scene chatScene = new Scene(chatPanel, 400, 500); // Adjust size as needed
            chatStage.setScene(chatScene);
            chatStage.show();
        } catch (Exception e) {
            System.err.println("Failed to show chat panel:");
            e.printStackTrace();
        }
    }

     // Helper method for hover animation
    private void setupBackButtonHoverAnimation(StackPane buttonPane) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(100), buttonPane);
        scaleIn.setToX(1.2); // Scale up to 120%
        scaleIn.setToY(1.2);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), buttonPane);
        scaleOut.setToX(1.0); // Scale back to 100%
        scaleOut.setToY(1.0);

        buttonPane.setOnMouseEntered(event -> scaleIn.playFromStart());
        buttonPane.setOnMouseExited(event -> scaleOut.playFromStart());
    }

    // Helper method for click handler
    private void setupBackButtonClickHandler(StackPane buttonPane) {
        buttonPane.setOnMouseClicked(event -> {
            try {
                mainApp.showLoginPage(); // Call the method to show login page
            } catch (java.io.IOException ex) {
                System.err.println("Failed to return to login page: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

}
