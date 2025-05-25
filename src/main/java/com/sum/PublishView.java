package com.sum;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.sum.view.FeedView;
import javafx.stage.FileChooser;
import java.io.File;
import java.sql.SQLException;
import com.sum.model.Note;
import com.sum.service.NoteService;

/**
 * 发布笔记视图
 */
public class PublishView {
    private VBox publishForm;
    private TextField titleField;
    private TextArea descriptionArea;
    private ImageView imagePreview;
    private String selectedImagePath;
    private NoteService noteService;

    public PublishView() {
        noteService = new NoteService();
        initializeUI();
    }

    /**
     * 初始化UI组件
     */
    private void initializeUI() {
        publishForm = new VBox(20);
        publishForm.getStyleClass().add("publish-form");
        publishForm.setPadding(new Insets(30));
        
        // 标题输入
        Label titleLabel = new Label("标题");
        titleLabel.getStyleClass().add("form-label");
        titleField = new TextField();
        titleField.setPromptText("给你的笔记起个标题吧");
        
        // 描述输入
        Label descriptionLabel = new Label("描述");
        descriptionLabel.getStyleClass().add("form-label");
        descriptionArea = new TextArea();
        descriptionArea.setPromptText("分享你的想法...");
        descriptionArea.setPrefRowCount(5);
        descriptionArea.setWrapText(true);
        
        // 图片上传区域
        VBox imageUploadBox = new VBox(10);
        imageUploadBox.getStyleClass().add("image-upload-container");
        
        Label imageLabel = new Label("封面图片");
        imageLabel.getStyleClass().add("form-label");
        
        imagePreview = new ImageView();
        imagePreview.setFitWidth(300);
        imagePreview.setFitHeight(300);
        imagePreview.setPreserveRatio(true);
        
        Button uploadButton = new Button("选择图片");
        uploadButton.setOnAction(e -> selectImage());
        
        imageUploadBox.getChildren().addAll(imageLabel, imagePreview, uploadButton);
        
        // 发布按钮
        Button publishButton = new Button("发布笔记");
        publishButton.getStyleClass().add("publish-button");
        publishButton.setOnAction(e -> publishNote());
        
        publishForm.getChildren().addAll(
            titleLabel, titleField,
            descriptionLabel, descriptionArea,
            imageUploadBox,
            publishButton
        );
    }

    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择图片");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("图片文件", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        File selectedFile = fileChooser.showOpenDialog(publishForm.getScene().getWindow());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            Image image = new Image(selectedFile.toURI().toString());
            imagePreview.setImage(image);
        }
    }

    private void publishNote() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        if (title.isEmpty()) {
            showAlert("请输入标题");
            return;
        }
        
        if (description.isEmpty()) {
            showAlert("请输入描述");
            return;
        }
        
        if (selectedImagePath == null) {
            showAlert("请选择封面图片");
            return;
        }
        
        try {
            String author = "当前用户"; // TODO: 使用实际的用户信息
            String authorAvatar = "/default-avatar.png"; // TODO: 使用实际的用户头像
            int imageHeight = 300; // 默认图片高度
            
            noteService.createNote(title, description, selectedImagePath, author, authorAvatar, imageHeight);
            
            // 清空表单
            titleField.clear();
            descriptionArea.clear();
            imagePreview.setImage(null);
            selectedImagePath = null;
            
            showSuccess("发布成功！");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("发布失败，请稍后重试");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("成功");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView() {
        return publishForm;
    }
}
