package com.sum.view;

import cn.hutool.core.date.DateUtil;
import com.sum.dialog.AvatarStackPane;
import com.sum.dialog.NoteDetailDialog;
import com.sum.domain.entity.CommentsInfo;
import com.sum.domain.entity.Notes;
import com.sum.domain.entity.RecordInfo;
import com.sum.service.INotesService;
import com.sum.service.IRecordService;
import com.sum.utils.ContextUtil;
import com.sum.utils.FileUtils;
import com.sum.utils.SpringContextUtil;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;


@Getter
public class MessageView {

    private VBox messageList;
    private BorderPane root;
    private IRecordService recordService;
    private INotesService notesService;

    public MessageView() {
        initializeUI();
    }

    private void initializeUI() {
        this.recordService = SpringContextUtil.getBean(IRecordService.class);
        this.notesService = SpringContextUtil.getBean(INotesService.class);
        messageList = new VBox();
        messageList.setPadding(new Insets(20,5,5,5));
        this.root = new BorderPane();
        createCenter();
        messageList.getChildren().add(this.root);
    }

    private void createCenter() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-border-width:0px;-fx-border-color: transparent;");
        scrollPane.setPrefHeight(700);
        VBox content = new VBox();
        content.setPadding(new Insets(10));
        content.setPrefWidth(800);
        content.setSpacing(8);
        //查询该笔记的所有评论
        List<RecordInfo> infoList = this.getRecordService().queryRecordInfo(ContextUtil.getCurrentUser().getUsername());
        for (RecordInfo recordInfo : infoList) {
            HBox messageBox = new HBox();
            AvatarStackPane avatarStackPane = new AvatarStackPane(50, recordInfo.getAvatarUrl(), false);
            messageBox.getChildren().add(avatarStackPane.getMainLayout());
            VBox v = new VBox();
            v.setSpacing(20);
            v.setPrefHeight(40);
            Label author = new Label(recordInfo.getOperator());
            FontIcon icon = new FontIcon("ci-user-avatar-filled-alt");
            icon.setIconSize(12);
            author.setGraphic(icon);
            author.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;");
            Text text = new Text();
            //点赞
            if (1 == recordInfo.getType()) {
                text.setText("给您的作品点了个赞❥(^_-)");
            } else {
                text.setText("评论了你：" + recordInfo.getContent());
            }
            text.setLineSpacing(10);
            text.setWrappingWidth(700);
            Label time = new Label("时间：" + DateUtil.format(recordInfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            time.setStyle("-fx-font-size: 10px;-fx-text-fill: #8a8a8a");
            //展示帖子图片
            ImageView noteImage = FileUtils.createImageView(recordInfo.getImageUrl());
            showNoteInfo(noteImage,recordInfo.getNoteId());
            noteImage.setFitWidth(100);
            noteImage.setFitHeight(100);
            noteImage.setSmooth(true);
            v.getChildren().addAll(author, text, time);
            messageBox.getChildren().addAll(v,noteImage);
            content.getChildren().add(messageBox);
        }
        scrollPane.setContent(content);
        this.root.setCenter(scrollPane);
    }

    private void showNoteInfo(ImageView noteImage, Long noteId) {
        noteImage.setCursor(Cursor.HAND);
        noteImage.setOnMouseClicked(mouseEvent -> {
            Notes note = this.notesService.getById(noteId);
            NoteDetailDialog noteDetailDialog = new NoteDetailDialog(note);
            noteDetailDialog.getNoteInfoStage().show();
        });
    }

}