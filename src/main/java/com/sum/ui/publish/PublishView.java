package com.sum.ui.publish;

import com.sum.service.NoteService;
import com.sum.view.FeedView;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;

public class PublishView {
    private final Stage stage;
    private FeedView feedView;
    private VBox mainLayout;
    private StackPane imageUploadPane;
    private ImageView selectedImageView;
    private TextField titleField;
    private TextArea descriptionArea;
    private Button publishButton;

    private File selectedImageFile;

    private final NoteService noteService;

    public PublishView() {
        this.stage = new Stage();
        this.noteService = new NoteService();
        this.mainLayout = new VBox(10);
        this.mainLayout.setPadding(new Insets(20));
        this.mainLayout.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        createImageUploadStep();

        Scene scene = new Scene(mainLayout, 400, 300);
        stage.setTitle("发布笔记");
        stage.setScene(scene);
    }

    public PublishView(FeedView feedView) {
        this();
        this.feedView = feedView;
    }

    private void createImageUploadStep() {
        imageUploadPane = new StackPane();
        imageUploadPane.setPrefSize(350, 200);
        imageUploadPane.setStyle(
                "-fx-border-color: #cccccc; " +
                "-fx-border-width: 2; " +
                "-fx-border-style: dashed; " +
                "-fx-background-color: #f0f0f0;" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;"
        );
        imageUploadPane.setAlignment(javafx.geometry.Pos.CENTER);

        Text uploadText = new Text("点击上传图片");
        uploadText.setStyle("-fx-font-size: 18px; -fx-fill: #666666;");

        imageUploadPane.getChildren().add(uploadText);

        imageUploadPane.setOnMouseClicked(event -> openImageChooser());

        mainLayout.getChildren().clear();
        mainLayout.getChildren().add(imageUploadPane);
    }

    private void createInputFieldsStep() {
        mainLayout.getChildren().clear();
        mainLayout.setAlignment(javafx.geometry.Pos.TOP_LEFT);

        selectedImageView = new ImageView();
        if (selectedImageFile != null) {
            try {
                Image image = new Image(selectedImageFile.toURI().toString());
                selectedImageView.setImage(image);
                selectedImageView.setFitWidth(350); // Adjust as needed
                selectedImageView.setPreserveRatio(true);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle potential image loading errors
            }
        }

        titleField = new TextField();
        titleField.setPromptText("添加标题");
        titleField.setPrefHeight(40);
        titleField.setStyle("-fx-font-size: 16px;");

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("添加正文");
        descriptionArea.setPrefHeight(150);
        descriptionArea.setStyle("-fx-font-size: 16px;");

        publishButton = new Button("发布");
        publishButton.setStyle("-fx-background-color: #ff2442; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10 20; -fx-background-radius: 5;");
        publishButton.setOnAction(event -> handlePublish());

        mainLayout.getChildren().addAll(selectedImageView, titleField, descriptionArea, publishButton);
    }

    private void openImageChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择图片");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("图片文件", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("所有文件", "*.*")
        );

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            selectedImageFile = file;
            createInputFieldsStep(); // Move to the next step
        }
    }

    private void handlePublish() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String imageUrl = (selectedImageFile != null) ? selectedImageFile.toURI().toString() : "";
        String author = "示例用户"; // TODO: 获取实际作者信息
        String authorAvatar = ""; // TODO: 获取实际作者头像URL
        int imageHeight = (int) selectedImageView.getFitHeight(); // 使用图片的实际高度或设定一个默认值

        if (title.isEmpty() || description.isEmpty()) {
            showAlert("错误", "请填写标题和正文");
            return;
        }

        try {
            noteService.createNote(
                title,
                description,
                author,
                imageUrl,
                authorAvatar,
                imageHeight
            );

            if (feedView != null) {
                feedView.refreshFeed();
            }

            stage.close();
            showAlert("成功", "笔记发布成功！");
        } catch (SQLException ex) {
            showAlert("错误", "发布失败：" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void show() {
        stage.show();
    }

    public VBox getView() {
        return mainLayout;
    }
} 