package com.sum.view;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.context.ApplicationContext;
import com.sum.JavaFxSMApplication;
import com.sum.MainApp;


@Getter
@Setter
public class MainFrameView {

    private Stage mainFrameStage;
    private TabPane tabPane;
    private Tab exploreTab;

    public MainFrameView() {
        mainFrameStage = new Stage();
        // 创建主界面布局
        BorderPane root = new BorderPane();

        // 创建标签页容器
        tabPane = new TabPane();  // 提前初始化 tabPane

        // 创建各个功能模块的标签页
        exploreTab = new Tab("发现");
        FontIcon icon1 = new FontIcon("codicon-search");
        exploreTab.setGraphic(icon1);

        Tab publishTab = new Tab("发布");
        FontIcon icon2 = new FontIcon("ci-cursor-2");
        publishTab.setGraphic(icon2);

        Tab messageTab = new Tab("消息");
        FontIcon icon3 = new FontIcon("ci-notebook-reference");

        messageTab.setGraphic(icon3);
        Tab profileTab = new Tab("我的");
        FontIcon icon4 = new FontIcon("ci-user-avatar-filled");
        profileTab.setGraphic(icon4);

        // 为每个标签页设置内容
        FeedView feedView = new FeedView(null);
        exploreTab.setContent(feedView.getView());

        // 将标签页添加到标签页容器中
        tabPane.getTabs().addAll(exploreTab, publishTab, messageTab, profileTab);
        tabPane.getTabs().forEach(tab -> {
            tab.setStyle("-fx-cursor:hand");
            tab.setClosable(false);
        });
        tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                // PublishView 构造函数需要 FeedView 参数
                PublishView publishView = new PublishView(feedView, tabPane);
                publishTab.setContent(publishView.getView());
            }
            if (newValue.intValue() == 2) {
                MessageView messageView = new MessageView();
                messageTab.setContent(messageView.getMessageList());
            }
            if (newValue.intValue() == 3) {
                ProfileView profileView = new ProfileView();
                profileTab.setContent(profileView.getView());
            }
        });
        tabPane.getStylesheets().add("css/main.css");
        // 创建顶部搜索栏
        VBox topBar = createTopBar();
        // 将组件添加到主布局中
        root.setTop(topBar);
        root.setCenter(tabPane);
        // 创建场景并显示
        Scene scene = new Scene(root, 972, 810);
        //mainFrameStage.setResizable(false);
        mainFrameStage.centerOnScreen();
        mainFrameStage.setTitle("Serendipity");
        mainFrameStage.setScene(scene);
    }

    private VBox createTopBar() {
        VBox topBar = new VBox(10);
        topBar.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(16);
        TextField searchField = new TextField();
        searchField.setPromptText("搜索笔记...");
        searchField.setPrefWidth(300);
        searchField.setPrefHeight(30);
        searchField.getStyleClass().add("search-oval");
        Button searchButton = new Button("搜索");
        searchButton.setStyle("-fx-background-color: #E7E7E7;-fx-border-color: #B5B5B5;-fx-border-radius: 3px;-fx-font-size: 14px;");
        searchButton.setPrefSize(70, 30);
        FontIcon fontIcon = new FontIcon("ci-search");
        fontIcon.setIconSize(14);
        searchButton.setGraphic(fontIcon);
        searchButton.setCursor(Cursor.HAND);
        searchButton.setOnAction(event -> {
            String keyWords = searchField.getText();
            this.tabPane.getSelectionModel().select(0);
            FeedView feedView = new FeedView(keyWords);
            exploreTab.setContent(feedView.getView());
        });
        hBox.getChildren().addAll(searchField, searchButton);
        VBox spacing = new VBox();
        spacing.setPrefHeight(20);
        topBar.getChildren().addAll(hBox, spacing);
        return topBar;
    }

    // 显示心情选择页面
    private void showMoodSelectPage() {
        try {
            // 获取MainApp实例
            ApplicationContext context = JavaFxSMApplication.getApplicationContext();
            MainApp mainApp = context.getBean(MainApp.class);
            // 使用当前登录用户ID，不再硬编码
            
            // 关闭当前窗口
            mainFrameStage.close();
            
            // 显示心情选择页面
            mainApp.showMoodSelectView();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("无法打开心情功能");
            alert.setContentText("出现错误: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
