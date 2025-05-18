package com.sum;

import com.sum.dao.community_interactions;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * 发布笔记视图
 */
public class PublishView {
    private VBox publishForm;
    private TextField titleField;
    private TextArea contentArea;

    public PublishView() {

        initializeUI();
        setupEventHandlers();
    }

    /**
     * 初始化UI组件
     */
    private void initializeUI() {
        // 创建ScrollPane以支持滚动
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white; -fx-border-color: transparent;");
        titleField = new TextField();
        contentArea = new TextArea();
        // 初始化发布表单布局this.titleField = new TextField();
//        this.contentArea = new TextArea();

        publishForm = new VBox(25);
        publishForm.getStyleClass().add("publish-form");
        publishForm.setPadding(new Insets(30, 25, 30, 25));

        // 页面标题
        Label pageTitle = new Label("发布笔记");
        pageTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        pageTitle.setPadding(new Insets(0, 0, 15, 0));

        // 分隔线
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #f0f0f0;");

        // 标题标签和输入框
        Label titleLabel = new Label("标题");
        titleLabel.getStyleClass().add("form-label");

        titleField.setPromptText("添加标题（让更多人看到你的笔记）");
        titleField.getStyleClass().add("text-field");
        titleField.setPrefHeight(45);

        // 内容标签和文本区域
        Label contentLabel = new Label("笔记内容");
        contentLabel.getStyleClass().add("form-label");

        contentArea.setPromptText("分享你的故事、经历或心得...");
        contentArea.setPrefRowCount(8);
        contentArea.setWrapText(true);
        contentArea.getStyleClass().add("text-area");

        // 图片上传区域
        Label imageLabel = new Label("添加图片");
        imageLabel.getStyleClass().add("form-label");

        // 图片网格展示区
        TilePane imageGrid = new TilePane();
        imageGrid.setPrefColumns(4);
        imageGrid.setHgap(12);
        imageGrid.setVgap(12);
        imageGrid.setPrefRows(2);
        imageGrid.getStyleClass().add("image-upload-container");

        // 添加上传按钮
        Button uploadButton = new Button("+");
        uploadButton.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-text-fill: #999;" +
                        "-fx-font-size: 28px;" +
                        "-fx-min-width: 90px;" +
                        "-fx-min-height: 90px;" +
                        "-fx-border-color: #ddd;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-style: dashed;" +
                        "-fx-background-radius: 8px;"
        );

        // 示例已上传图片
        for (int i = 0; i < 3; i++) {
            StackPane imageContainer = new StackPane();
            ImageView image = new ImageView(new Image("https://picsum.photos/seed/" + i + "/90/90"));
            image.setFitWidth(90);
            image.setFitHeight(90);
            image.setStyle("-fx-background-radius: 8px;");

            Button removeBtn = new Button("×");
            removeBtn.setStyle(
                    "-fx-background-color: rgba(0,0,0,0.6);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-min-width: 24px;" +
                            "-fx-min-height: 24px;" +
                            "-fx-padding: 0;" +
                            "-fx-background-radius: 12px;"
            );
            StackPane.setAlignment(removeBtn, Pos.TOP_RIGHT);
            StackPane.setMargin(removeBtn, new Insets(6, 6, 0, 0));

            imageContainer.getChildren().addAll(image, removeBtn);
            imageGrid.getChildren().add(imageContainer);
        }

        imageGrid.getChildren().add(uploadButton);

        // 位置信息
        HBox locationBox = new HBox(10);
        locationBox.setAlignment(Pos.CENTER_LEFT);
        locationBox.setStyle("-fx-cursor: hand;");

        Label locationIcon = new Label("📍");
        Label locationLabel = new Label("添加位置");
        locationLabel.setTextFill(Color.valueOf("#ff2442"));
        locationLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        locationBox.getChildren().addAll(locationIcon, locationLabel);

        // 标签信息
        HBox tagBox = new HBox(10);
        tagBox.setAlignment(Pos.CENTER_LEFT);
        tagBox.setStyle("-fx-cursor: hand;");

        Label tagIcon = new Label("#");
        Label tagLabel = new Label("添加标签");
        tagLabel.setTextFill(Color.valueOf("#ff2442"));
        tagLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        tagBox.getChildren().addAll(tagIcon, tagLabel);

        // 商品信息
        HBox productBox = new HBox(10);
        productBox.setAlignment(Pos.CENTER_LEFT);
        productBox.setStyle("-fx-cursor: hand;");

        Label productIcon = new Label("🛍");
        Label productLabel = new Label("添加商品");
        productLabel.setTextFill(Color.valueOf("#ff2442"));
        productLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        productBox.getChildren().addAll(productIcon, productLabel);

        // 附加选项区域
        HBox optionsBox = new HBox(25);
        optionsBox.setPadding(new Insets(15, 0, 15, 0));
        optionsBox.getChildren().addAll(locationBox, tagBox, productBox);

        // 提交按钮
        Button publishButton = new Button("发布");
        publishButton.getStyleClass().add("publish-button");
        HBox buttonBox = new HBox(publishButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(25, 0, 0, 0));

        // 将所有元素添加到表单中
        publishForm.getChildren().addAll(
                pageTitle,
                separator,
                titleLabel, titleField,
                contentLabel, contentArea,
                imageLabel, imageGrid,
                optionsBox,
                buttonBox
        );

        scrollPane.setContent(publishForm);
    }

    /**
     * 获取发布笔记的表单布局
     *
     * @return 表单布局
     */
    public VBox getPublishForm() {
        return publishForm;
    }


    //    实现点击提交发布功能
//    1、获取到发布按钮、2、设置发布按钮点击事件 3、执行发布操作 4、提示发布成功
    private void setupEventHandlers() {
//        获取到发布按钮
        Button publishButton = (Button) publishForm.lookup(".publish-button");
//        设置发布按钮点击事件
        publishButton.setOnAction(event -> {
            String title = titleField.getText();
            String content = contentArea.getText();

            if (title.isEmpty() || content.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setContentText("标题和内容不能为空！");
                alert.showAndWait();
            } else {
                // 执行发布操作
                community_interactions.createInteraction(title, content);
                // 提示发布成功
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("成功");
                alert.setHeaderText(null);
                alert.setContentText("发布成功！");
                alert.showAndWait();
            }
        });
    }
}
