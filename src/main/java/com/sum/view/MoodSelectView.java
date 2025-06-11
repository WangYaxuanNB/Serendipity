package com.sum.view;

import com.sum.MainApp;
import com.sum.service.IMoodService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.animation.ScaleTransition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * 心情选择界面
 */
@Component
public class MoodSelectView {

    private MainApp mainApp;
    private Scene scene;
    private VBox root;
    private VBox moodCardDisplayArea; // 用于显示当前心情卡片的区域
    private int currentMoodIndex = 0; // 当前显示的心情索引
    private double dragStartX;
    private static final double DRAG_THRESHOLD = 100.0; // 拖动阈值
    
    @Autowired
    private IMoodService moodService;

    // 预设心情数据 (英文名称，中文名称，图片名，背景颜色/样式 - 简化处理)
    private static final List<String[]> MOODS = Arrays.asList(
            new String[]{"Angry", "生气", "angry.png", "-fx-background-color: #FF7043;"}, // 示例颜色
            new String[]{"Upset", "烦躁", "upset.png", "-fx-background-color: #7986CB;"}, // 示例颜色
            new String[]{"Sad", "伤心", "sad.png", "-fx-background-color: #A1E7EB;"},
            new String[]{"Good", "开心", "good.png", "-fx-background-color: #FFCA28;"}, // 示例颜色
            new String[]{"Happy", "兴奋", "happy.png", "-fx-background-color:#DFEBFF;"},// 示例颜色
            new String[]{"Spectacular", "心动", "spectacular.png", "-fx-background-color:rgb(255, 171, 199);"} // 示例颜色
    );
    
    public MoodSelectView() {
        // 默认构造函数，Spring会调用
    }

    public void initialize(MainApp mainApp) {
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
        
        try {
            // 尝试加载返回图标
            Image backArrowImage = new Image(getClass().getResourceAsStream("/icons/back_arrow.png"));
            if (backArrowImage.isError()) {
                throw new Exception("图标加载失败");
            }
            ImageView backArrowIcon = new ImageView(backArrowImage);
            backArrowIcon.setFitWidth(30);
            backArrowIcon.setFitHeight(30);
            backElementPane = new StackPane(backArrowIcon);
        } catch (Exception e) {
            System.err.println("返回图标加载失败: " + e.getMessage());
            // 使用文本作为返回按钮的备选方案
            Label backLabel = new Label("← 返回");
            backLabel.setFont(Font.font("Arial", 16));
            backLabel.setStyle("-fx-text-fill: #333333;");
            backElementPane = new StackPane(backLabel);
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
            mainApp.showCalendarView();
        });

        // 客服按钮
        Button chatButton = new Button("人工客服");
        chatButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 20; -fx-padding: 5 15;");
        chatButton.setOnAction(e -> showChatView());

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
        Label moodNameLabel = new Label(englishName); // 显示英文名称
        moodNameLabel.setFont(Font.font("Arial", 24));
        moodNameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;"); // 文字颜色示例

        // 心情图标
        ImageView moodIcon = new ImageView();
        try {
            // 尝试使用与MoodSelectPage相同的资源路径格式
            Image img = new Image(getClass().getResource("/mood/" + imageName).toExternalForm());
            moodIcon.setImage(img);
        } catch (Exception e) {
            System.out.println("图标加载失败: " + imageName);
            // 尝试备用路径
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/mood/" + imageName));
                moodIcon.setImage(img);
            } catch (Exception ex) {
                System.out.println("备用路径图标加载也失败: " + ex.getMessage());
                // 如果图片加载失败，可以使用占位符或者显示错误信息
            }
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
        
        // 使用服务保存记录
        long userId = mainApp.getCurrentUserId(); // 获取当前登录用户ID
        boolean success = moodService.saveMoodRecord(userId, moodText, moodImage);
        
        if (success) {
            System.out.println("心情记录保存成功: " + moodText + ", 图片: " + moodImage);
        } else {
            System.out.println("心情记录保存失败: " + moodText);
        }
    }

    // Method to show the chat panel
    private void showChatView() {
        try {
            Stage chatStage = new Stage();
            chatStage.setTitle("人工客服");
            ChatPanel chatPanel = new ChatPanel();
            Scene chatScene = new Scene(chatPanel.getRoot(), 400, 500);
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
                mainApp.showLoginPage(); // 返回登录页面，与MoodSelectPage一致
            } catch (Exception ex) {
                System.err.println("返回登录页面失败: " + ex.getMessage());
                ex.printStackTrace();
                mainApp.showMainFrame(); // 如果失败，则返回主界面
            }
        });
    }
} 