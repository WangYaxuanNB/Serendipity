package com.sum;

import com.sum.view.FeedView;
import com.sum.ui.publish.PublishView;
import com.sum.MessageView;
import com.sum.ProfileView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.net.URL;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 创建主界面布局
            BorderPane root = new BorderPane();
    
            // 创建标签页容器
            TabPane tabPane = new TabPane();  // 提前初始化 tabPane
    
            // 创建各个功能模块的标签页
            Tab exploreTab = createTab("发现", "🔍");
            Tab publishTab = createTab("发布", "✨");
            Tab messageTab = createTab("消息", "💬");
            Tab profileTab = createTab("我", "👤");
    
            // 为每个标签页设置内容
            FeedView feedView = new FeedView();
            exploreTab.setContent(feedView.getView());
    
            // PublishView 构造函数需要 FeedView 参数
            PublishView publishView = new PublishView(feedView);
            publishTab.setContent(publishView.getView());
    
            // 假设 MessageView 构造函数不需要参数，并且有 getView() 方法
            MessageView messageView = new MessageView();
            messageTab.setContent(messageView.getView());
    
            // 假设 ProfileView 构造函数不需要参数，并且有 getView() 方法
            ProfileView profileView = new ProfileView();
            profileTab.setContent(profileView.getView());
    
            // 将标签页添加到标签页容器中
            tabPane.getTabs().addAll(exploreTab, publishTab, messageTab, profileTab);
    
            // 修复：确保选中状态的样式正确应用
            tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                for (Tab t : tabPane.getTabs()) {
                    VBox box = (VBox) t.getGraphic();  // 修改: 使用 t.getGraphic() 而不是 t.getUserData()
                    if (t == newTab) {
                        // 注意：这里的样式可能需要调整以适应新的标签页结构
                        // box.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: red;");
                    } else {
                        // box.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: transparent;");
                    }
                }
            });
    
            // 创建顶部搜索栏
            HBox topBar = createTopBar();
    
            // 将组件添加到主布局中
            root.setTop(topBar);
            root.setCenter(tabPane);
    
            // 创建场景并显示
            Scene scene = new Scene(root, 800, 600);
    
            // 加载CSS样式
            URL cssUrl = getClass().getResource("/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
                System.out.println("CSS loaded successfully: " + cssUrl.toExternalForm());
            } else {
                System.out.println("CSS file not found!");
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
     * @param title 标签文字
     * @param icon 图标文字（使用Emoji）
     * @return 创建的标签页
     */
    private Tab createTab(String title, String icon) {
        VBox tabBox = new VBox(10);
        tabBox.setAlignment(Pos.CENTER);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 12px;");

        tabBox.getChildren().addAll(iconLabel, titleLabel);

        Tab tab = new Tab();
        tab.setGraphic(tabBox);

        // 特殊处理"首页推荐"标签页，移除关闭按钮
        // if ("首页推荐".equals(title)) {
        //     tab.setClosable(false);
        // }

        return tab;
    }

    /**
     * 创建顶部搜索栏
     * @return 顶部搜索栏
     */
    private HBox createTopBar() {
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("搜索笔记...");
        searchField.setPrefWidth(300);
        searchField.getStyleClass().add("search-oval");

        Button searchButton = new Button("搜索");
        searchButton.setOnAction(event -> {
            String query = searchField.getText();
            System.out.println("搜索: " + query);
            // TODO: 实现搜索功能
        });

        topBar.getChildren().addAll(searchField, searchButton);

        return topBar;
    }

    public static void main(String[] args) {
        launch(args);
    }
}