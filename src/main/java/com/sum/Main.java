package com.sum;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    private BorderPane root;
    private TabPane tabPane;

    @Override
    public void start(Stage primaryStage) {
        try {
            // 初始化 tabPane
            tabPane = new TabPane();

            // 创建各个 Tab
            Tab feedTab = createTab("首页推荐", "🏠");
            Tab exploreTab = createTab("发现", "🔍");
            Tab publishTab = createTab("发布", "➕");
            Tab messageTab = createTab("消息", "💬");
            Tab profileTab = createTab("我", "👤");

            // 设置内容（你已有的模块调用）
            feedTab.setContent(new FeedView().getFeedGrid());
            exploreTab.setContent(new NoteDetailView().getNoteDetailView());
            publishTab.setContent(new PublishView().getPublishForm());
            messageTab.setContent(new MessageView().getMessageList());
            profileTab.setContent(new ProfileView().getProfileInfo());

            // 添加所有 Tab
            tabPane.getTabs().addAll(feedTab, exploreTab, publishTab, messageTab, profileTab);

            // 选中标签高亮样式控制
            tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                Tab selectedTab = newTab;
                for (Tab t : tabPane.getTabs()) {
                    VBox box = (VBox) t.getUserData();
                    if (t == selectedTab) {
                        box.setStyle(
                                "-fx-border-width: 0 0 2 0;" +
                                        "-fx-border-color: red;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-focus-color: transparent;" +
                                        "-fx-faint-focus-color: transparent;"
                        );
                    } else {
                        box.setStyle(
                                "-fx-border-width: 0 0 2 0;" +
                                        "-fx-border-color: transparent;" +
                                        "-fx-font-weight: normal;" +
                                        "-fx-focus-color: transparent;" +
                                        "-fx-faint-focus-color: transparent;"
                        );
                    }
                }
            });

            // 顶部搜索栏
            HBox topBar = createTopBar();

            // 主布局
            root = new BorderPane();
            root.setTop(topBar);
            root.setCenter(tabPane);

            // 场景和样式
            Scene scene = new Scene(root, 800, 600);
            URL cssUrl = getClass().getResource("/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            primaryStage.setTitle("Serendipity");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建带图标的标签页
     */
    private Tab createTab(String title, String icon) {
        VBox tabBox = new VBox(2);
        tabBox.setAlignment(Pos.CENTER);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 11px;");

        tabBox.getChildren().addAll(iconLabel, titleLabel);

        Tab tab = new Tab();
        tab.setGraphic(tabBox);
        tab.setClosable(false); // 所有 tab 不可关闭
        tab.setUserData(tabBox);

        // 默认未选中状态样式
        tabBox.setStyle(
                "-fx-border-width: 0 0 2 0;" +
                        "-fx-border-color: transparent;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-faint-focus-color: transparent;"
        );

        return tab;
    }

    /**
     * 创建顶部搜索栏
     */
    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setAlignment(Pos.CENTER);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #f0f0f0; -fx-border-width: 0 0 1px 0;");

        Button searchButton = new Button("🔍  搜索笔记、用户、话题");
        searchButton.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-background-radius: 18px;" +
                        "-fx-padding: 8 60;" +
                        "-fx-text-fill: #999;" +
                        "-fx-font-size: 13px;"
        );

        topBar.getChildren().add(searchButton);
        return topBar;
    }

    public static void main(String[] args) {
        launch(args);
    }
}