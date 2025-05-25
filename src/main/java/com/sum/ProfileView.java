package com.sum;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

/**
 * 个人中心视图
 */
public class ProfileView {
    private VBox profileInfo;
    
    public ProfileView() {
        initializeUI();
    }
    
    private void initializeUI() {
        // 初始化个人中心布局
        profileInfo = new VBox(20);
        profileInfo.getStyleClass().add("profile-info");
        
        // 用户头像部分
        HBox avatarBox = new HBox(10);
        Label avatarLabel = new Label("头像:");
        ImageView avatarView = new ImageView(new Image("https://via.placeholder.com/80x80?text=Avatar"));
        avatarView.setFitWidth(80);
        avatarView.setFitHeight(80);
        avatarView.setPreserveRatio(true);
        avatarBox.getChildren().addAll(avatarLabel, avatarView);
        
        // 用户名
        Label usernameLabel = new Label("用户名: XiaoHongShuUser");
        
        // 笔记数量统计
        Label noteCountLabel = new Label("笔记数: 42");
        
        // 粉丝数量统计
        Label followerCountLabel = new Label("粉丝数: 1500");
        
        // 创建简介部分
        VBox bioBox = new VBox(5);
        Label bioTitle = new Label("个人简介:");
        Label bioContent = new Label("热爱分享生活的Java开发者，喜欢旅行、美食和科技产品。");
        bioContent.setWrapText(true);
        bioBox.getChildren().addAll(bioTitle, bioContent);
        
        // 设置内边距
        profileInfo.setPadding(new Insets(20));
        
        // 将所有元素添加到布局中
        profileInfo.getChildren().addAll(
            avatarBox,
            usernameLabel,
            noteCountLabel,
            followerCountLabel,
            bioBox
        );
    }
    
    /**
     * 获取个人中心视图的根节点
     * @return 个人中心布局
     */
    public VBox getView() {
        return profileInfo;
    }
}