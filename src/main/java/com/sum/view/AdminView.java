package com.sum.view;

import com.sum.MainApp;
import com.sum.domain.entity.User;
import com.sum.service.IUserService;
import com.sum.utils.ControlUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 管理员界面
 */
@Component
public class AdminView {

    private Stage adminStage;
    private MainApp mainApp;
    private BorderPane rootLayout;
    private VBox contentArea;
    private Scene scene;

    @Autowired
    private IUserService userService;

    @Autowired
    private MoodRecordManagementPanel moodRecordManagementPanel;

    @Autowired
    private SensitiveInfoManagementPanel sensitiveInfoManagementPanel;

    public Stage getAdminStage() {
        return adminStage;
    }

    /**
     * 初始化管理员界面
     */
    public void initialize(MainApp mainApp) {
        this.mainApp = mainApp;
        this.adminStage = new Stage();
        adminStage.setTitle("管理员界面");
        
        // 创建主布局
        rootLayout = new BorderPane();
        rootLayout.setPadding(new Insets(15));
        
        // 设置背景和圆角
        BackgroundFill rootBackgroundFill = new BackgroundFill(
                Color.WHITE, new CornerRadii(26), Insets.EMPTY);
        rootLayout.setBackground(new Background(rootBackgroundFill));
        
        // 创建顶部标题
        Label titleLabel = new Label("管理员界面");
        titleLabel.setFont(Font.font("SimHei", FontWeight.BOLD, 22));
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setPadding(new Insets(10, 0, 20, 0));
        rootLayout.setTop(titleLabel);
        
        // 创建左侧导航菜单
        VBox sideMenu = createSideMenu();
        rootLayout.setLeft(sideMenu);
        
        // 创建中心内容区域
        contentArea = new VBox(10);
        contentArea.setPadding(new Insets(0, 0, 0, 20)); // 左侧留出空间
        contentArea.setAlignment(Pos.TOP_LEFT);
        rootLayout.setCenter(contentArea);
        
        // 创建场景
        scene = new Scene(rootLayout, 800, 600);
        scene.setFill(Color.TRANSPARENT);
        
        // 设置舞台
        adminStage.setScene(scene);
        adminStage.setResizable(false);
        adminStage.initStyle(StageStyle.DECORATED);
        
        // 默认显示用户管理
        showUserManagement();
    }
    
    /**
     * 获取场景
     */
    public Scene getScene() {
        return scene;
    }
    
    /**
     * 创建左侧导航菜单
     */
    private VBox createSideMenu() {
        VBox sideMenu = new VBox(20);
        sideMenu.setPadding(new Insets(10, 20, 10, 10));
        sideMenu.setPrefWidth(120);
        sideMenu.setStyle("-fx-border-color: #E0E0E0; -fx-border-width: 0 1 0 0;");
        
        Label userManagementLabel = new Label("用户管理");
        Label moodRecordManagementLabel = new Label("心情记录管理");
        Label sensitiveInfoManagementLabel = new Label("敏感信息管理");
        
        String menuItemStyle = "-fx-font-family: 'SimHei'; -fx-font-size: 14px;  -fx-cursor: hand;";
        userManagementLabel.setStyle(menuItemStyle);
        moodRecordManagementLabel.setStyle(menuItemStyle);
        sensitiveInfoManagementLabel.setStyle(menuItemStyle);
        
        // 添加点击事件
        userManagementLabel.setOnMouseClicked(event -> {
            resetMenuStyle();
            userManagementLabel.setStyle(menuItemStyle + "-fx-font-weight: bold;");
            showUserManagement();
        });
        
        moodRecordManagementLabel.setOnMouseClicked(event -> {
            resetMenuStyle();
            moodRecordManagementLabel.setStyle(menuItemStyle + "-fx-font-weight: bold;");
            showMoodRecordManagement();
        });
        
        sensitiveInfoManagementLabel.setOnMouseClicked(event -> {
            resetMenuStyle();
            sensitiveInfoManagementLabel.setStyle(menuItemStyle + "-fx-font-weight: bold;");
            showSensitiveInfoManagement();
        });
        
        sideMenu.getChildren().addAll(
                userManagementLabel,
                moodRecordManagementLabel,
                sensitiveInfoManagementLabel
        );
        
        return sideMenu;
    }
    
    /**
     * 重置菜单样式
     */
    private void resetMenuStyle() {
        String menuItemStyle = "-fx-font-family: 'SimHei'; -fx-font-size: 14px; -fx-cursor: hand;";
        for (javafx.scene.Node node : ((VBox) rootLayout.getLeft()).getChildren()) {
            if (node instanceof Label) {
                node.setStyle(menuItemStyle);
            }
        }
    }
    
    /**
     * 显示用户管理面板
     */
    private void showUserManagement() {
        contentArea.getChildren().clear();
        
        // 在用户管理面板中处理所有用户相关逻辑
        UserManagementPanel userManagementPanel = new UserManagementPanel(userService);
        contentArea.getChildren().add(userManagementPanel);
    }
    
    /**
     * 显示心情记录管理面板
     */
    private void showMoodRecordManagement() {
        contentArea.getChildren().clear();
        moodRecordManagementPanel.initialize();
        contentArea.getChildren().add(moodRecordManagementPanel);
    }
    
    /**
     * 显示敏感信息管理面板
     */
    private void showSensitiveInfoManagement() {
        contentArea.getChildren().clear();
        sensitiveInfoManagementPanel.initialize();
        contentArea.getChildren().add(sensitiveInfoManagementPanel);
    }
} 