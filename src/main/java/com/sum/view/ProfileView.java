package com.sum.view;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.sum.dialog.AvatarStackPane;
import com.sum.domain.entity.User;
import com.sum.service.IUserService;
import com.sum.utils.ContextUtil;
import com.sum.utils.FileUtils;
import com.sum.utils.SpringContextUtil;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

/**
 * 个人中心视图
 */
@Getter
public class ProfileView {
    private VBox profileInfo;
    private AvatarStackPane avatar;
    private IUserService userService;
    private User user;
    private Button save;

    public ProfileView() {
        this.userService = SpringContextUtil.getBean(IUserService.class);
        // 初始化个人中心布局
        profileInfo = new VBox(10);
        profileInfo.setPadding(new Insets(20, 0, 0, 0));
        profileInfo.setAlignment(Pos.TOP_CENTER);
        VBox card = new VBox();
        card.setMaxWidth(584);
        card.setPrefHeight(580);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        // 添加阴影效果
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(8);
        shadow.setOffsetX(0);
        shadow.setOffsetY(3);
        card.setEffect(shadow);
        // 添加悬停效果
        card.setOnMouseEntered(e -> {
            shadow.setRadius(12);
            shadow.setColor(Color.rgb(0, 0, 0, 0.3));
            card.setTranslateY(-5);
        });

        card.setOnMouseExited(e -> {
            shadow.setRadius(8);
            shadow.setColor(Color.rgb(0, 0, 0, 0.2));
            card.setTranslateY(0);
        });
        this.user = ContextUtil.getCurrentUser();
        // 用户头像部分
        avatar = new AvatarStackPane(150, user.getAvatarUrl(),true);
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(30);
        Label usernameLabel = new Label("用户名");
        Label username = new Label(user.getUsername());
        Label emailL = new Label("邮箱");
        Label email = new Label(user.getEmail());
        Label registerL = new Label("注册日期");
        Label registerDate = new Label(DateUtil.format(user.getRegistrationTime(), "yyyy-MM-dd"));

        Label bioTitle = new Label("个人简介");
        Label bioContent = new Label("热爱分享生活的Java开发者，喜欢旅行、美食和科技产品。");

        GridPane.setHalignment(usernameLabel, HPos.RIGHT);
        gridPane.add(usernameLabel, 0, 0);
        GridPane.setHalignment(username, HPos.LEFT);
        gridPane.add(username, 1, 0);

        GridPane.setHalignment(emailL, HPos.RIGHT);
        gridPane.add(emailL, 0, 1);
        GridPane.setHalignment(email, HPos.LEFT);
        gridPane.add(email, 1, 1);

        GridPane.setHalignment(registerL, HPos.RIGHT);
        gridPane.add(registerL, 0, 2);
        GridPane.setHalignment(registerDate, HPos.LEFT);
        gridPane.add(registerDate, 1, 2);

        GridPane.setHalignment(bioTitle, HPos.RIGHT);
        gridPane.add(bioTitle, 0, 3);
        GridPane.setHalignment(bioContent, HPos.LEFT);
        gridPane.add(bioContent, 1, 3);
        this.save = new Button("保存");
        this.save.setCursor(Cursor.HAND);
        FontIcon fontIcon = new FontIcon("ci-add-filled");
        fontIcon.setIconSize(16);
        fontIcon.setIconColor(Paint.valueOf("#FFF"));
        HBox saveBox = new HBox();
        saveBox.setPadding(new Insets(60));
        this.save.setGraphic(fontIcon);
        this.save.setStyle("-fx-background-color: #ff2442; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10 20; -fx-background-radius: 5;");
        this.save.setVisible(false);
        saveBox.setAlignment(Pos.CENTER);
        saveBox.getChildren().add(this.save);
        // 将所有元素添加到布局中
        card.getChildren().addAll(avatar.getMainLayout(), gridPane, saveBox);
        profileInfo.getChildren().addAll(card);
        profileInfo.getStylesheets().add("css/profile.css");
        modifyAvatar();
    }

    /**
     * 修改头像
     */
    private void modifyAvatar() {
        this.getAvatar().getEditButton().setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择头像");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("图片文件", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                    new FileChooser.ExtensionFilter("所有文件", "*.*")
            );
            File file = fileChooser.showOpenDialog(new Stage());
            if (ObjectUtil.isNotNull(file)) {
                this.save.setVisible(true);
                Image image = new Image(file.toURI().toString());
                this.getAvatar().getImageView().setImage(image);
                this.save.setOnMouseClicked(mouseEvent -> {
                    String avatarUrl = FileUtils.uploadImage(file);
                    this.user.setAvatarUrl(avatarUrl);
                    this.getUserService().updateById(this.user);
                    this.save.setVisible(false);
                });
            } else {
                this.save.setVisible(false);
                this.getAvatar().getImageView().setImage(new Image("images/default_avatar.png"));
            }
        });
    }

    /**
     * 获取个人中心视图的根节点
     *
     * @return 个人中心布局
     */
    public VBox getView() {
        return profileInfo;
    }
}