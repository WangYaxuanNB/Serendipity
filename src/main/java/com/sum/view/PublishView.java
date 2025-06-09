package com.sum.view;

import com.sum.domain.entity.Notes;
import com.sum.service.INotesService;
import com.sum.utils.ContextUtil;
import com.sum.utils.ControlUtil;
import com.sum.utils.FileUtils;
import com.sum.utils.SpringContextUtil;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

@Getter
public class PublishView extends VBox{
    private FeedView feedView;
    private VBox mainLayout;
    private HBox imageUploadPane;
    private ImageView selectedImageView;
    private TextField titleField;
    private TextArea descriptionArea;
    private Button publishButton;
    private File selectedImageFile;
    private INotesService notesService;
    private TabPane tabPane;
    private Text uploadText;

    public PublishView() {
        this.notesService = SpringContextUtil.getBean(INotesService.class);
        this.mainLayout = new VBox(10);
        this.mainLayout.setAlignment(Pos.CENTER);
        createImageUploadStep();
    }

    public PublishView(FeedView feedView, TabPane tabPane) {
        this();
        this.tabPane = tabPane;
        this.feedView = feedView;
    }

    private void createImageUploadStep() {
        imageUploadPane = new HBox();
        imageUploadPane.setPadding(new Insets(0,10,10,10));
        this.imageUploadPane.setMaxWidth(400);
        this.imageUploadPane.setPrefHeight(400);
        imageUploadPane.setStyle(
                "-fx-border-color: #cccccc; " +
                "-fx-border-width: 2; " +
                "-fx-border-style: dashed; " +
                "-fx-background-color: #f0f0f0;" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;"
        );
        imageUploadPane.setAlignment(Pos.CENTER);
        uploadText = new Text("点击上传图片");
        uploadText.setStyle("-fx-font-size: 18px; -fx-fill: #666666;");
        imageUploadPane.setCursor(Cursor.HAND);
        imageUploadPane.getChildren().add(uploadText);
        imageUploadPane.setOnMouseClicked(event -> openImageChooser());
        mainLayout.getChildren().add(imageUploadPane);
        createInputFieldsStep();
    }

    private void createInputFieldsStep() {
        selectedImageView = new ImageView();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        Label title = new Label("标题");
        title.setStyle("-fx-font-size: 16px;");
        titleField = new TextField();
        titleField.setPromptText("请输入标题");
        titleField.setPrefHeight(40);
        titleField.setStyle("-fx-font-size: 16px;");
        descriptionArea = new TextArea();
        Label content = new Label("内容");
        content.setStyle("-fx-font-size: 16px;");
        descriptionArea.setPromptText("请输入内容");
        descriptionArea.setPrefHeight(150);
        GridPane.setHalignment(title, HPos.LEFT);
        gridPane.add(title,0,0);
        GridPane.setHalignment(titleField, HPos.RIGHT);
        gridPane.add(titleField,1,0);

        GridPane.setHalignment(content, HPos.LEFT);
        gridPane.add(content,0,1);
        GridPane.setHalignment(descriptionArea, HPos.RIGHT);
        gridPane.add(descriptionArea,1,1);

        descriptionArea.setStyle("-fx-font-size: 16px;");
        publishButton = new Button("发布");
        publishButton.setCursor(Cursor.HAND);
        FontIcon fontIcon = new FontIcon("ci-add-filled");
        fontIcon.setIconSize(16);
        fontIcon.setIconColor(Paint.valueOf("#FFF"));
        publishButton.setGraphic(fontIcon);
        publishButton.setStyle("-fx-background-color: #ff2442; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10 20; -fx-background-radius: 5;");
        publishButton.setOnAction(event -> handlePublish());
        mainLayout.getChildren().addAll(selectedImageView, gridPane, publishButton);
    }

    private void openImageChooser() {
        this.imageUploadPane.getChildren().clear();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择图片");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("图片文件", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("所有文件", "*.*")
        );
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            this.imageUploadPane.setStyle(
                    "-fx-border-color: transparent; " +
                            "-fx-border-width: 0; " +
                            "-fx-background-color: transparent;"
            );
            selectedImageFile = file;
            mainLayout.setAlignment(Pos.CENTER);
            Image image = new Image(selectedImageFile.toURI().toString());
            selectedImageView.setImage(image);
            selectedImageView.setFitWidth(400);
            selectedImageView.setFitHeight(400);
            selectedImageView.setPreserveRatio(false);
            selectedImageView.setSmooth(true);
            this.imageUploadPane.getChildren().add(selectedImageView);
        }else {
            this.imageUploadPane.setStyle(
                    "-fx-border-color: #cccccc; " +
                            "-fx-border-width: 2; " +
                            "-fx-border-style: dashed; " +
                            "-fx-background-color: #f0f0f0;" +
                            "-fx-background-radius: 5;" +
                            "-fx-border-radius: 5;"
            );
            this.imageUploadPane.getChildren().add(uploadText);
        }
    }

    private void handlePublish() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        if(null == selectedImageFile){
            ControlUtil.warning("请选择图片！");
            return;
        }
        //上传图片
        String imageUrl = FileUtils.uploadImage(selectedImageFile);
        // 使用图片的实际高度或设定一个默认值
        int imageHeight = (int) selectedImageView.getFitHeight();
        if (title.isEmpty() || description.isEmpty()) {
            ControlUtil.warning("请填写标题和内容！");
            return;
        }
        Notes note = new Notes();
        note.setTitle(title);
        note.setDescription(description);
        note.setAuthor(ContextUtil.getCurrentUser().getUsername());
        note.setImageUrl(imageUrl);
        note.setImageHeight(imageHeight);
        this.getNotesService().save(note);
        if (feedView != null) {
            feedView.refreshFeed();
        }
        ControlUtil.success("笔记发布成功！");
        this.tabPane.getSelectionModel().select(0);
    }

    public VBox getView() {
        return mainLayout;
    }
}