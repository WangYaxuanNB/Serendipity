package com.sum.dialog;

import com.sum.utils.FileUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import lombok.Getter;

@Getter
public class AvatarStackPane {

    private VBox mainLayout;
    private Label editButton;
    private ImageView imageView;


    public AvatarStackPane(double size, String imageUrl, Boolean flag) {
        // 创建主布局
        mainLayout = new VBox(30);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);
        StackPane avatarContainer = new StackPane();
        imageView = FileUtils.createAvatarImageView(imageUrl);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        // 创建圆形裁剪
        Circle clip = new Circle(size / 2, size / 2, size / 2);
        imageView.setClip(clip);
        // 添加阴影效果
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(8);
        imageView.setEffect(shadow);
        // 创建半透明覆盖层（初始不可见）
        Circle overlay = new Circle(size / 2, size / 2, size / 2);
        overlay.setFill(Color.rgb(0, 0, 0, 0.5));
        overlay.setVisible(false);
        // 创建编辑按钮（初始不可见）
        editButton = new Label("编辑");
        editButton.setTextFill(Color.WHITE);
        editButton.setFont(Font.font(12));
        editButton.setPadding(new Insets(8, 16, 8, 16));
        editButton.setStyle("-fx-background-color: transparent;");
        editButton.setVisible(false);
        if (flag) {
            editButton.setCursor(Cursor.HAND);
            // 添加鼠标事件监听
            avatarContainer.setOnMouseEntered(e -> {
                overlay.setVisible(true);
                editButton.setVisible(true);
            });

            avatarContainer.setOnMouseExited(e -> {
                overlay.setVisible(false);
                editButton.setVisible(false);
            });
        }
        // 将所有元素添加到容器
        avatarContainer.getChildren().addAll(imageView, overlay, editButton);
        mainLayout.getChildren().add(avatarContainer);
    }
}
