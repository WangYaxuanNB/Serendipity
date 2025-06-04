package com.serendipity.demo;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

public class AdminPage {

    private MainApp mainApp;
    private Scene scene;
    private BorderPane rootLayout;
    private VBox sideMenu;
    private VBox contentArea;

    public AdminPage(MainApp mainApp) {
        this.mainApp = mainApp;

        rootLayout = new BorderPane();
        rootLayout.setPadding(new Insets(20));
        
        BackgroundFill rootBackgroundFill = new BackgroundFill(
                Color.WHITE, new CornerRadii(26), Insets.EMPTY);
        rootLayout.setBackground(new Background(rootBackgroundFill));

        // 顶部标题
        Label titleLabel = new Label("管理员界面");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        rootLayout.setTop(titleLabel);

        // 左侧导航
        sideMenu = new VBox(15);
        sideMenu.setPadding(new Insets(10));
        sideMenu.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 1 0 0;");
        sideMenu.setPrefWidth(150);

        Label userManagementLabel = new Label("用户管理");
        Label moodRecordManagementLabel = new Label("心情记录管理");
        Label sensitiveInfoManagementLabel = new Label("敏感信息管理");

        sideMenu.getChildren().addAll(
                userManagementLabel,
                moodRecordManagementLabel,
                sensitiveInfoManagementLabel
        );

        String menuItemStyle = "-fx-font-size: 16px; -fx-padding: 5 0;";
        sideMenu.getChildren().forEach(node -> node.setStyle(menuItemStyle + "-fx-cursor: hand;"));

        rootLayout.setLeft(sideMenu);

        // 中心内容区域
        contentArea = new VBox(10);
        contentArea.setPadding(new Insets(10));
        contentArea.setAlignment(Pos.TOP_LEFT);
        contentArea.getChildren().add(new Label("请从左侧菜单选择管理功能"));
        rootLayout.setCenter(contentArea);

        // 添加用户管理标签的事件处理
        userManagementLabel.setOnMouseClicked(event -> {
            showUserManagement();
        });

        // 添加心情记录管理标签的事件处理
        moodRecordManagementLabel.setOnMouseClicked(event -> {
            showMoodRecordManagement();
        });

        // 添加敏感信息管理标签的事件处理
        sensitiveInfoManagementLabel.setOnMouseClicked(event -> {
            showSensitiveInfoManagement();
        });

        this.scene = new Scene(rootLayout, 800, 600);
        scene.setFill(Color.web("#D9D9D9"));
    }

    public Scene getScene() {
        return scene;
    }

    // 显示用户管理面板
    private void showUserManagement() {
        contentArea.getChildren().clear(); // 清除中心区域原有内容
        contentArea.getChildren().add(new UserManagementPanel()); // 添加用户管理面板
    }

    // 显示心情记录管理面板
    private void showMoodRecordManagement() {
        contentArea.getChildren().clear(); // 清除中心区域原有内容
        contentArea.getChildren().add(new MoodRecordManagementPanel()); // 添加心情记录管理面板
    }

    // 显示敏感信息管理面板
    private void showSensitiveInfoManagement() {
        contentArea.getChildren().clear(); // 清除中心区域原有内容
        contentArea.getChildren().add(new SensitiveInfoManagementPanel()); // 添加敏感信息管理面板
    }
   //test
    // TODO: 添加其他管理功能的显示方法
} 